import { NativeModules } from "react-native";

const { Pax } = NativeModules;

export default {
	FULL_CUT: 0,
	PARTIAL_CUT: 1,

	printStr(text, cutMode) {
		Pax.printStr(text, cutMode === undefined ? 0 : cutMode);
	},
	openDrawer() {
		return Pax.openDrawer();
	},
	printBitmap(inputValue, smallerDimension) {
		Pax.printBitmap(inputValue, smallerDimension);
	},
	setCardSwipeListener(callback) {
		DeviceEventEmitter.addListener('onCardSwiped', cardData => {
		callback && callback(cardData);
		});
		Pax.setCardSwipeListener();
	},
	readCard() {
		return Pax.readCard();
	},
	resetCardReader(){
		return Pax.resetCardReader();
	}
};
