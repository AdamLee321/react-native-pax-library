
package com.reactpaxlibrary;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
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
    public void closeMagReader() {
        try {
            mag.close();
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }

    @ReactMethod
    public boolean magCardIsSwiped() {
        try {
            return mag.isSwiped();
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }

    @ReactMethod
    public void openMagReader() {
        try {
            mag.open();
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }

    @ReactMethod
    public void readFromMagReader(Promise promise) {
        try {
            TrackData data = mag.read();

            WritableMap trackDataMap = Arguments.createMap();
            trackDataMap.putString("track1", data.getTrack1());
            trackDataMap.putString("track2", data.getTrack2());
            trackDataMap.putString("track3", data.getTrack3());
            trackDataMap.putString("track4", data.getTrack4());
            trackDataMap.putInt("resultCode", data.getResultCode());
            promise.resolve(trackDataMap);
        } catch (MagDevException e) {
            promise.reject("ERROR", e.getMessage());
        }
    }

    @ReactMethod
    public TrackData readExtFromMagReader() {
        try {
            return mag.readExt();
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }

    @ReactMethod
    public void resetMagReader() {
        try {
            mag.reset();
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }

    @ReactMethod
    public void setupMagReader(byte flag) {
        try {
            mag.setup(flag);
        } catch (MagDevException e) {
            throw new RuntimeException(e);
        }
    }
}
