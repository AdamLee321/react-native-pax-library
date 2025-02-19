declare var Pax: {
	FULL_CUT: number;
	PARTIAL_CUT: number;

	printStr: (text: string, cutMode?: number) => void;
	openDrawer: () => Promise<any>;
	printBitmap: (inputValue: string, smallerDimension: number) => void;
	closeMagReader: () => void;
	magCardIsSwiped: () => boolean;
	openMagReader: () => void;
	readFromMagReader: () => Promise<any>;
	readExtFromMagReader: () => any;
	resetMagReader: () => void;
	setupMagReader: (flag: number) => void;
	pollCardReader: (timeout: number) => Promise<any>;
};

export default Pax;
