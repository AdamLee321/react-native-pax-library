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
	closeMagReader() {
        	Pax.closeMagReader();
	},
	magCardIsSwiped() {
		return Pax.magCardIsSwiped();
	},
    	openMagReader() {
        	Pax.openMagReader();
    	},
    	readFromMagReader() {
        	return Pax.readFromMagReader();
    	},
    	readExtFromMagReader() {
        	return Pax.readExtFromMagReader();
    	},
    	resetMagReader() {
        	Pax.resetMagReader();
    	},
    	setupMagReader(flag) {
        	Pax.setupMagReader(flag);
    	},
};
