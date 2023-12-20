
package com.reactpaxlibrary;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.pax.dal.ICashDrawer;
import com.pax.dal.IDAL;
import com.pax.dal.IMag;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.TrackData;
import com.pax.dal.exceptions.MagDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;
import android.graphics.Bitmap;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class RNPaxLibraryModule extends ReactContextBaseJavaModule {

    private static final String NAME = "Pax";
    private final ReactApplicationContext reactContext;
    private QRGEncoder qrgEncoder;
    private IDAL dal;
    private IMag mag;
    private IPrinter printer;
    private ICashDrawer cashDrawer;

    public RNPaxLibraryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        try {
            dal = NeptuneLiteUser.getInstance().getDal(reactContext);
            mag = dal.getMag();
            printer = dal.getPrinter();
            cashDrawer = dal.getCashDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void printStr(String text, Double cutMode) {
        try {
            printer.init();
            printer.setGray(3);
            printer.printStr(text, null);
            printer.start();
            printer.cutPaper(cutMode.intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void printBitmap(String inputValue, int smallerDimension) {
        try {
            QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
            Bitmap bitmap = qrgEncoder.getBitmap();
            printer.init();
            printer.printBitmap(bitmap);
            printer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void openDrawer(Promise promise) {
        final int result = cashDrawer.open();

        if (result == 0) {
            promise.resolve(result);
        } else {
            promise.reject("Error " + result, "The cash drawer cannot be opened.");
        }
    }

    @ReactMethod
    public void setCardSwipeListener() {
        try {
            mag.open();

            // Check if the card is swiped
            if (mag.isSwiped()) {
                TrackData cardData = mag.read();

                // Create a dictionary to store track data components
                WritableMap trackDataMap = Arguments.createMap();
                trackDataMap.putString("track1", cardData.getTrack1());
                trackDataMap.putString("track2", cardData.getTrack2());
                trackDataMap.putString("track3", cardData.getTrack3());
                trackDataMap.putString("track4", cardData.getTrack4());

                // Emit an event to React Native with card data
                getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onCardSwiped",trackDataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mag.close();
            } catch (MagDevException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ReactMethod
    public void readCard() {
        // This method can be used independently if you want to manually trigger card reading.
        setCardSwipeListener();
    }

    @ReactMethod
    public void resetCardReader() {
        // This method can be used to reset magnetic stripe card reader, and clear buffer of magnetic stripe card.
        try {
            mag.reset();
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }
}
