package com.tssoftgroup.tmobile.screen;

/**
 *
 * HelloWorld.java
 * The sentinal sample!
 *
 * Copyright � 1998-2008 Research In Motion Ltd.
 *
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ChoiceField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.tssoftgroup.tmobile.component.EditFieldwithFocus;
import com.tssoftgroup.tmobile.component.LabelFieldWithFullBG;
import com.tssoftgroup.tmobile.component.MainListVerticalFieldManager;
import com.tssoftgroup.tmobile.component.MyButtonField;
import com.tssoftgroup.tmobile.component.TrainingListField;
import com.tssoftgroup.tmobile.component.engine.Engine;
import com.tssoftgroup.tmobile.component.engine.LoadPicThread;
import com.tssoftgroup.tmobile.model.TrainingInfo;
import com.tssoftgroup.tmobile.utils.Const;
import com.tssoftgroup.tmobile.utils.Img;
import com.tssoftgroup.tmobile.utils.MyColor;

/**
 * Create a new screen that extends MainScreen, which provides default standard
 * behavior for BlackBerry applications.
 */
/*
 * BlackBerry applications that provide a user interface must extend
 * UiApplication.
 */
public class TrainingListScreen extends FixMainScreen implements
		FieldChangeListener {
	Img imgstock = Img.getInstance();
	private MainItem _mainMenuItem = new MainItem();
	String topic = "";
	boolean alreadyPush = false;
	static TrainingListScreen instance;

	TrainingListField _list;

	EditFieldwithFocus searchTextField;

	Vector all = new Vector();
	public LoadPicThread loader;
	private ChoiceField catChoice = null;

	public static TrainingListScreen getInstance() {
		if (instance == null) {
			instance = new TrainingListScreen();
		}
		return instance;
	}

	private TrainingListScreen() {
		super(MODE_TRAIN);
		nextBT.setChangeListener(this);
		previousBT.setChangeListener(this);
		createVideoMain();
	}

	public TrainingListScreen(String topic) {
		super(MODE_TRAIN);
		nextBT.setChangeListener(this);
		previousBT.setChangeListener(this);
		this.topic = topic;
		createVideoMain();

	}
	Vector allCats = new Vector();
	VerticalFieldManager catManager = new VerticalFieldManager();
	Vector allTrains = new Vector();
	public void setList(Vector trainInfos, String cat) {
		allTrains = trainInfos;
		allCats = new Vector();
		allCats.addElement("All");
		_list.removeAll();
		all.removeAllElements();
		int ind = 0;
		for (int i = 0; i < trainInfos.size(); i++) {
			TrainingInfo tinfo = (TrainingInfo) trainInfos.elementAt(i);
			if (cat == null) {
				if(!tinfo.getCat().equals("")){
					_list.add(tinfo);
					all.addElement(tinfo);
				}
			} else {
				if (tinfo.getCat().equals(cat)) {
					_list.add(tinfo);
					all.addElement(tinfo);
				}
			}
			boolean dup = false;
			for (int j = 0; j < allCats.size(); j++) {
				String c = (String)allCats.elementAt(j);
				if(c.equals(tinfo.getCat())){
					dup = true;
				}
			}
			if(!dup && !tinfo.getCat().equals("")){
				allCats.addElement(tinfo.getCat());
			}
		}
		if (catChoice != null  ) {
			try{
				catManager.delete(catChoice);
			}catch(IllegalArgumentException e){
				// Delete item that is not belong to 
			}
		}
		// Set Category
		System.out.println("allcats " + allCats.size());
		catChoice = new ChoiceField("Category :", allCats.size(), 0) {
			public Object getChoice(int index) throws IllegalArgumentException {
				System.out.println("a1 test" + allCats.size() );
				System.out.println("a2 test" + index);
				if(allCats.size() > index){
					return allCats.elementAt(index);	
				}else{
					return "";
				}
				
			}

			
		};
		for (int i = 0; i < allCats.size(); i++) {
			if(((String)allCats.elementAt(i)).equals(cat)){
				ind = i;
			}
		}
		catChoice.setSelectedIndex(ind);
		catChoice.setChangeListener(this);
		catChoice.setMargin(0, 50 * Display.getWidth() / 480, 0,25 * Display.getWidth() / 480 );
		catChoice.setPadding(0, 50 * Display.getWidth() / 480, 0,0);
		catChoice.setFont(MyColor.FONT_SEARCH);
		catManager.add(catChoice);
	}

	public boolean navigationClick(int status, int time) {
		System.out.println("navigationClick");
		if (_list.isMyFocus) {
			if ((status & KeypadListener.STATUS_TRACKWHEEL) != KeypadListener.STATUS_TRACKWHEEL) {
				selectAction();
				return true;
			}
		}
		return super.navigationClick(status, time);
	}

	private void handleSearchType() {
		if (searchTextField.isFocus) {
			String text = searchTextField.getText();
			_list.removeAll();
			_list.invalidate();
			for (int i = 0; i < all.size(); i++) {
				TrainingInfo picInfo = (TrainingInfo) all.elementAt(i);
				if (picInfo.containKey(text)) {
					_list.add(picInfo);
				}
			}
			_list.invalidate();
		}
	}

	private void selectAction() {
		if (_list != null && _list.getSize() > 0) {

			if (_list.get(_list, _list.getSelectedIndex()) != null) {
				if (loader != null) {
					// loader.stop();
					loader.myWait();
				}
				TrainingInfo trainInfo = (TrainingInfo) _list.get(_list, _list
						.getSelectedIndex());
				if (trainInfo != null) {
					TrainingVideoScreen scr = new TrainingVideoScreen(trainInfo);
					UiApplication.getUiApplication().pushScreen(scr);
				}
			}
		}
	}

	public void createVideoMain() {
		// videoList = HttpConn.getList(topic, Const.type_movie);
		XYEdges edge = new XYEdges(24, 25, 8, 25);

		Bitmap img = imgstock.getHeader();
		BitmapField bf = new BitmapField(img, BitmapField.FIELD_HCENTER
				| BitmapField.USE_ALL_WIDTH);
		add(bf);

		try {
			RadioButtonGroup rgrp = new RadioButtonGroup();

			edge = new XYEdges(5, 25 * Display.getWidth() / 480, 2,
					25 * Display.getWidth() / 480);

			HorizontalFieldManager mainHorizontalManager = new HorizontalFieldManager(
					HorizontalFieldManager.FIELD_HCENTER
							| HorizontalFieldManager.USE_ALL_WIDTH
							| HorizontalFieldManager.FIELD_VCENTER);
			mainHorizontalManager.setMargin(edge);
			// EditField
			searchTextField = new EditFieldwithFocus(
					"Search: ",
					topic,
					35,
					BasicEditField.FIELD_HCENTER | BasicEditField.FIELD_VCENTER,
					MyColor.FONT_SEARCH, MyColor.SEARCH_COLOR) {
				public void layout(int width, int height) {
					super.layout(getPreferredWidth(), getPreferredHeight());
					setExtent(getPreferredWidth(), getPreferredHeight());
				}

				public int getPreferredHeight() {
					return 25;
				}

				public int getPreferredWidth() {
					return 340 * Display.getWidth() / 480;
				}
			};
			mainHorizontalManager.add(searchTextField);
			mainHorizontalManager.add(searchBT);
			// edit.setMaxSize(35);
			// edit.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
			// add(edit);

			// Topic Photo and List field
			MainListVerticalFieldManager videoManager = new MainListVerticalFieldManager();
			videoManager.add(mainHorizontalManager);
			videoManager.add(catManager);
			// bf.setMargin(edge);
			// videoManager.add(bf);
			LabelField topicLabel = new LabelFieldWithFullBG("Training",
					MyColor.FONT_TOPIC, MyColor.FONT_TOPIC_COLOR,
					MyColor.TOPIC_BG, Const.LABEL_WIDTH);
			edge = new XYEdges(2, 25 * Display.getWidth() / 480, 2,
					25 * Display.getWidth() / 480);
			topicLabel.setMargin(edge);
			videoManager.add(topicLabel);
			_list = new TrainingListField();
			videoManager.add(_list);
			pagingManager.setMargin(edge);
			videoManager.add(pagingManager);
			add(videoManager);

		} catch (Exception e) {
			System.out.println("" + e.toString());
		}

		edge = new XYEdges(5, 0, 0, 0);
		// bf = new BitmapField(img, Field.FIELD_BOTTOM | Field.USE_ALL_HEIGHT);
		// bf.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
		// add(bf);
		addMenuItem(_mainMenuItem);
	}

	protected void onExposed() {
		if (loader != null) {
			loader.myResume();
		}
	}

	protected void onUiEngineAttached(boolean attached) {
		if (attached) {
			if (loader != null) {
				loader.myResume();
			}
		}
		if (_list != null) {
			this.invalidate();
			// fieldManager.invalidate();
			_list.invalidate();
		}

		super.onUiEngineAttached(attached);
	}

	private final class MainItem extends MenuItem {
		/**
		 * Constructor.
		 */
		private MainItem() {
			super("Main Menu", 100, 1);
		}

		/**
		 * Attempts to save the screen's data to its associated memo. If
		 * successful, the edit screen is popped from the display stack.
		 */
		public void run() {
			UiApplication.getUiApplication().popScreen(
					UiApplication.getUiApplication().getActiveScreen());
		}
	}

	protected boolean keyDown(int arg0, int arg1) {
		// TODO Auto-generated method stub
		try {
			switch (arg0) {
			case 1179648:
				close();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		handleSearchType();
		return super.keyDown(arg0, arg1);
	}

	public boolean keyChar(char c, int status, int time) {
		handleSearchType();
		switch (c) {
		case Characters.ENTER:
			return true;
		case Characters.ESCAPE:
			UiApplication.getUiApplication().popScreen(
					UiApplication.getUiApplication().getActiveScreen());
			return true;
		default:
			boolean retValue = super.keyChar(c, status, time);
			handleSearchType();
			return retValue;
		}
	}

	public void fieldChanged(Field field, int context) {
		// Dialog.alert("test");
		if (field == catChoice) {
			System.out.println("cat choice size " + catChoice.getSize());
			System.out.println("cat choice ind" + catChoice.getSelectedIndex());
			
			String selectedCat = (String)catChoice.getChoice(catChoice.getSelectedIndex());
			System.out.println("11111");
			
			if(selectedCat.equals("All")){
				selectedCat = null;
			}
			setList(allTrains, selectedCat);
			System.out.println("2222");
			
			processHaveNext(numItem);
			System.out.println("33333");
			
//			Engine.trainingCat = (String)allCats.elementAt(catChoice.getSelectedIndex()); 
			return;
		}
		if (!(field instanceof MyButtonField)) {
			super.fieldChanged(field, context);
			return;
		}
		MyButtonField btnField = (MyButtonField) field;
		if (btnField == nextBT) {
			// Dialog.alert("next");
			currentIndex = currentIndex + Const.NUM_LIST;
			UiApplication.getUiApplication().pushScreen(
					WaitScreen.getInstance());
			Engine.getInstance().getTraining(currentIndex, search);
		} else if (btnField == previousBT) {
			// Dialog.alert("prev");
			currentIndex = currentIndex - Const.NUM_LIST;
			UiApplication.getUiApplication().pushScreen(
					WaitScreen.getInstance());
			Engine.getInstance().getTraining(currentIndex, search);
		} else if (btnField == searchBT) {
			search = searchTextField.getText();
			UiApplication.getUiApplication().pushScreen(
					WaitScreen.getInstance());
			Engine.getInstance().getTraining(currentIndex, search);
		}
	}
}
