declare var Pax: {
	FULL_CUT: number;
	PARTIAL_CUT: number;

	printStr: (text: string, cutMode?: number) => void;
	openDrawer: () => Promise<any>;
	printBitmap: (inputValue: string, smallerDimension: number) => void;
	setCardSwipeListener: (callback: (cardData: any) => void) => void;
	readCard: () => void;
	resetCardReader: () => void;
};

export default Pax;
