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

import java.util.Date;
import java.util.Vector;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.VolumeControl;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.tssoftgroup.tmobile.component.ButtonListener;
import com.tssoftgroup.tmobile.component.CrieLabelField;
import com.tssoftgroup.tmobile.component.LabelFieldWithFullBG;
import com.tssoftgroup.tmobile.component.MyButtonField;
import com.tssoftgroup.tmobile.component.MyPlayer;
import com.tssoftgroup.tmobile.component.ScreenWithComment;
import com.tssoftgroup.tmobile.component.engine.Engine;
import com.tssoftgroup.tmobile.component.engine.HttpUtilUploadThread;
import com.tssoftgroup.tmobile.model.Comment;
import com.tssoftgroup.tmobile.model.PicInfo;
import com.tssoftgroup.tmobile.utils.Const;
import com.tssoftgroup.tmobile.utils.Img;
import com.tssoftgroup.tmobile.utils.MyColor;
import com.tssoftgroup.tmobile.utils.Scale;
import com.tssoftgroup.tmobile.utils.Wording;

public class VideoConnectPlayerScreen extends MainScreen implements
		FieldChangeListener, PlayerListener, MyPlayer,ScreenWithComment {
	private MainItem _mainMenuItem = new MainItem();
	private MovieItem _videoItem = new MovieItem();
	private CrieLabelField _currentTime = null;
	private CrieLabelField _duration = null;
	private LabelField _volumeDisplay = null;
	private Player player = null;
	private TimerUpdateThread _timerUpdateThread = null;
	private String videoPath = "";
	private VideoControl videoControl = null;
	private VolumeControl volumeControl = null;
	Img imgStock = Img.getInstance();
	public PicInfo picinfo;
	public VerticalFieldManager commentsManager = new VerticalFieldManager();
	boolean isFullScreen = false;

	public void setFullScreen(boolean bool) {
		isFullScreen = bool;
	}
	int currentComment;
	
	public int getCurrentCommentInd() {
		return currentComment;
	}
	public void setCurrentCommentInd(int currentComment) {
		this.currentComment = currentComment;
	}
	public VideoConnectPlayerScreen(PicInfo picInfo) {
		this.picinfo = picInfo;
		XYEdges edge = new XYEdges(2, 0, 2, 0);

		Bitmap img = imgStock.getHeader();
		BitmapField bf = new BitmapField(img, BitmapField.FIELD_HCENTER
				| BitmapField.USE_ALL_WIDTH);
		add(bf);

		edge = new XYEdges(5, 25, 2, 10);
		// HorizontalFieldManager buttonHorizontalManager = new
		// HorizontalFieldManager(
		// HorizontalFieldManager.FIELD_HCENTER
		// | HorizontalFieldManager.USE_ALL_WIDTH);
		HorizontalFieldManager buttonHorizontalManager = new HorizontalFieldManager();
		edge = new XYEdges(2, 0, 0, 0);
		// HorizontalFieldManager videoHorizontalManager = new
		// HorizontalFieldManager(
		// HorizontalFieldManager.FIELD_HCENTER
		// | HorizontalFieldManager.USE_ALL_WIDTH);

		try {
			/*
             *
             */
			// boolean playerSupported = true;
			int volumeLevel;
			Field videoField;
			this.videoPath = picInfo.getVideoUrl();
			player = javax.microedition.media.Manager.createPlayer(picInfo
					.getVideoUrl()
					+ HttpUtilUploadThread.getConnectionSuffix());
			// }

			// if the player has been created; checking method for
			// initialization above
			if (player != null /* && player.getState() != Player.CLOSED */) {
				player.addPlayerListener(this);
				player.realize();
				player.prefetch();
				if ((videoControl = (VideoControl) player
						.getControl("VideoControl")) != null) {
					// Dialog.alert("videoControl not null");
					videoField = (Field) videoControl.initDisplayMode(
							VideoControl.USE_GUI_PRIMITIVE,
							"net.rim.device.api.ui.Field");
					// videoControl.setDisplaySize(300 * Display.getWidth() /
					// 480,
					// 200 * Display.getHeight() / 360);// edge = new
					videoControl.setDisplaySize(Const.VIDEO_WIDTH,
							Const.VIDEO_HEIGHT);// edge = new
					// videoControl.setDisplaySize(videoControl.getSourceWidth(),
					// videoControl.getSourceHeight());// edge = new
					// XYEdges(2, 0,
					// 0, 97);
					edge = new XYEdges(2, 0, 0,
							(Display.getWidth() - Const.VIDEO_WIDTH) / 2);
					// videoField.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
					videoField.setMargin(edge);
					_currentTime = new CrieLabelField("00:00", 0x00,
							Scale.VIDEO_PLAYER_TIME_HEIGHT, Field.FOCUSABLE);
					edge = new XYEdges(260 * Display.getWidth() / 480, 5, 0,
							15 * Display.getWidth() / 480);
					// _currentTime.setMargin(edge);
					_duration = new CrieLabelField("00:00", 0x00,
							Scale.VIDEO_PLAYER_TIME_HEIGHT, Field.FOCUSABLE);
					edge = new XYEdges(260 * Display.getWidth() / 480, 0, 0,
							5 * Display.getWidth() / 480);
					// _duration.setMargin(edge);
					add(videoField);
					videoControl.setVisible(true);
				} else {
				}
				if ((volumeControl = (VolumeControl) player
						.getControl("VolumeControl")) != null) {
					volumeControl.setMute(false);
					volumeControl.setLevel(Const.DEFAULT_VOLUMN);
					volumeLevel = volumeControl.getLevel();
				}

				// _volumeDisplay = new LabelField ("Volume : " +
				// _volumeControl.getLevel());
				if (player.getState() != player.STARTED) {
					player.start();
					_timerUpdateThread = new TimerUpdateThread();
					_timerUpdateThread.start();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
              *
              */
		// CustomButtonField playButton = new
		MyButtonField playButton = null;
		// if(player.getState()==player.STARTED){
		// playButton = new MyButtonField("Pause",ButtonField.ELLIPSIS);
		// }else{
		playButton = new MyButtonField("Stop", ButtonField.ELLIPSIS, true);
		// }
		// playButton.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
		playButton.setChangeListener(this);

		buttonHorizontalManager.add(playButton);

		// edge = new XYEdges(206, 5, 6, 24);
		// CustomButtonField fullButton = new
		MyButtonField fullButton = new MyButtonField("Full Screen",
				ButtonField.ELLIPSIS, true);
		// fullButton.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
		fullButton.setChangeListener(new ButtonListener(player, 9, this));
		buttonHorizontalManager.add(fullButton);

		// edge = new XYEdges(206, 5, 6, 5);
		// CustomButtonField stopButton = new
		// MyButtonField infoButton = new MyButtonField("More Info",
		// ButtonField.ELLIPSIS);
		// stopButton.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
		// infoButton.setChangeListener(this);
		// mainHorizontalManager.add(infoButton);

		// edge = new XYEdges(206, 24, 6, 24);
		// CustomButtonField commentButton = new
		MyButtonField commentButton = new MyButtonField("Comment",
				ButtonField.ELLIPSIS, true);
		// commentButton.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
		commentButton.setChangeListener(this);
		buttonHorizontalManager.add(commentButton);

		edge = new XYEdges(5, 0, 0, (Display.getWidth()
				- _currentTime.getPreferredWidth() - 3) / 2);
		HorizontalFieldManager timeManager = new HorizontalFieldManager();
		timeManager.add(_currentTime);
		timeManager.add(new CrieLabelField("/", 0x00,
				Scale.VIDEO_PLAYER_TIME_HEIGHT, Field.NON_FOCUSABLE));
		timeManager.add(_duration);
		timeManager.setMargin(edge);
		edge = new XYEdges(5, 0, 0,
				(Display.getWidth() - buttonHorizontalManager
						.getPreferredWidth()) / 2);
		buttonHorizontalManager.setMargin(edge);
		add(timeManager);
		add(buttonHorizontalManager);
		// bf = new BitmapField(img, Field.FIELD_BOTTOM | Field.USE_ALL_HEIGHT);
		// add(bf);

		addMenuItem(_mainMenuItem);
		addMenuItem(_videoItem);
		// addMenuItem( _exitFullItem );

	}

	protected void paintBackground(Graphics g) {
		/*
		 * if(bkgrndBmp != null){ g.rop(Graphics.ROP_SRC_ALPHA_GLOBALALPHA,
		 * (this.getWidth() / 2) - (bkgrndBmp .getWidth() /2), (this.getHeight()
		 * / 2) - (bkgrndBmp .getHeight() /2), bkgrndBmp .getWidth(), bkgrndBmp
		 * .getHeight(), bkgrndBmp ,0,0); }
		 */
		g.setBackgroundColor(Const.BG_COLOR);

		// Clears the entire graphic area to the current background
		g.clear();
		g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
	}

	protected boolean keyDown(int arg0, int arg1) {
		// TODO Auto-generated method stub

		return super.keyDown(arg0, arg1);
	}

	public boolean keyChar(char c, int status, int time) {
		boolean bool = false;
		try {
			// player.stop();
			// VideoControl videoControl = (VideoControl)
			// player.getControl("VideoControl");
			if (player != null && player.getState() == player.STARTED && isFullScreen) {
				bool = true;
				isFullScreen = false;
				videoControl.setDisplayFullScreen(false);
			}
			// player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (c) {
		case Characters.ENTER:
		case Characters.DELETE:
//		case Characters.BACKSPACE:
//			return true;
		case Characters.ESCAPE:
			if (!bool) {

				try {
					player.stop();
					// player.deallocate();
					// player.close();
					_timerUpdateThread.stop();
				} catch (Exception e) {
					System.out.println("" + e.toString());
				}
				UiApplication.getUiApplication().popScreen(
						UiApplication.getUiApplication().getActiveScreen());
			}

			return true;
		default:
			return super.keyChar(c, status, time);
		}
	}

	protected boolean keyControl(char c, int status, int time) {
		// Capture volume control key press and adjust volume accordingly.
		switch (c) {
		case Characters.CONTROL_VOLUME_DOWN:
			volumeControl.setLevel(volumeControl.getLevel() - 10);
			return true;

		case Characters.CONTROL_VOLUME_UP:
			volumeControl.setLevel(volumeControl.getLevel() + 10);
			return true;
		}

		return super.keyControl(c, status, time);
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
			try {
				player.stop();
				_timerUpdateThread.stop();
			} catch (Exception e) {
				System.out.println("" + e.toString());
			}
			UiApplication.getUiApplication().popScreen(
					VideoConnectPlayerScreen.this);
			UiApplication.getUiApplication().popScreen(
					UiApplication.getUiApplication().getActiveScreen());
		}
	}

	private final class MovieItem extends MenuItem {
		/**
		 * Constructor.
		 */
		private MovieItem() {
			super("Video Connect", 100, 1);
		}

		/**
		 * Attempts to save the screen's data to its associated memo. If
		 * successful, the edit screen is popped from the display stack.
		 */
		public void run() {
			try {
				player.stop();
				_timerUpdateThread.stop();
			} catch (Exception e) {
				System.out.println("" + e.toString());
			}
			UiApplication.getUiApplication().popScreen(
					VideoConnectPlayerScreen.this);
			// UiApplication.getUiApplication().popScreen(
			// UiApplication.getUiApplication().getActiveScreen() );
		}
	}

	public void fieldChanged(Field field, int context) {
		MyButtonField btnField = (MyButtonField) field;
		if (btnField.getLabel().equals("Start")) {
			try {
				// Start/resume the media player.
				player.start();

				_timerUpdateThread = new TimerUpdateThread();
				_timerUpdateThread.start();
				btnField.setLabel("Stop");
			} catch (MediaException pe) {
				System.out.println(pe.toString());
			}
		} else if (btnField.getLabel().equals("Stop")) {
			try {
				// Stop/pause the media player.
				player.stop();
				_timerUpdateThread.stop();
				btnField.setLabel("Start");
			} catch (MediaException pe) {
				System.out.println(pe.toString());
			}
		} else if (btnField.getLabel().equals("Comment")) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					if (!isAlreadyAddComment) {
						addCommentMoreInfo();
						isAlreadyAddComment = true;
					}
					commentLabelField.setFocus();
				}
			});

		}else if (btnField.getLabel().equals("Back")) {
			try {
			player.stop();
			_timerUpdateThread.stop();
			UiApplication.getUiApplication().popScreen(
					UiApplication.getUiApplication().getActiveScreen());
			} catch (MediaException pe) {
				System.out.println(pe.toString());
			}
		}
	}

	private Vector commentList = null;
	public boolean isAlreadyAddComment = false;
	public LabelField commentLabelField = new LabelFieldWithFullBG("comments",
			MyColor.COMMENT_LABEL_FONT, 0x00, MyColor.COMMENT_LABEL_BG, Display
					.getWidth()
					- 50 * Display.getWidth() / 480);
	LabelField postCommentLabel = new LabelFieldWithFullBG("post comment",
			MyColor.COMMENT_LABEL_FONT, 0x00, MyColor.COMMENT_LABEL_BG, Display
					.getWidth()
					- 50 * Display.getWidth() / 480);

	EditField postCommentTF = new EditField("", "");
	public void addComment() {
		postCommentTF.setText("");
		CrieLabelField commentLabel = new CrieLabelField("By "
				+ Engine.comment.getUser() + " at " + Engine.comment.getTime()
				+ ": ", MyColor.LIST_DESC_FONT,
				Scale.VIDEO_CONNECT_DETAIL_COMMENT_FONT_HEIGHT
						- (Display.getWidth() > 350 ? 8 : 2),
				LabelField.FOCUSABLE);
		commentLabel.isFix = true;
		XYEdges edge;
		edge = new XYEdges(2, 35 * Display.getWidth() / 480, 2, 35 * Display
				.getWidth() / 480);
		commentLabel.setMargin(edge);
		// commentLabel.setBorder(BorderFactory.createSimpleBorder(edge,
		// Border.STYLE_TRANSPARENT));
		commentsManager.add(commentLabel);
		commentLabel = new CrieLabelField(Engine.comment.getComment(), 0x00,
				Scale.VIDEO_CONNECT_DETAIL_COMMENT_FONT_HEIGHT,
				LabelField.FOCUSABLE);
		commentLabel.setMargin(edge);
		commentLabel.isFix = true;
		// commentLabel.setBorder(BorderFactory.createSimpleBorder(edge,
		// Border.STYLE_TRANSPARENT));
		commentsManager.add(commentLabel);
		FixMainScreen.processHaveComment(commentsManager, picinfo,
				this);
	}
	public void addCommentMoreInfo() {
		XYEdges edge = new XYEdges(24, 25, 8, 25);
		commentLabelField.setFont(Scale.FONT_DETAIL_TITLE);
		postCommentLabel.setFont(Scale.FONT_DETAIL_TITLE);

		edge = new XYEdges(Scale.EDGE, 25 * Display.getWidth() / 480,
				Scale.EDGE, 25 * Display.getWidth() / 480);
		commentLabelField.setMargin(edge);
		postCommentLabel.setMargin(edge);
		postCommentTF.setMargin(edge);
		add(commentLabelField);
		commentList = new Vector();

		// temp data
		for (int i = 0; i < picinfo.comments.size(); i++) {
			Comment commment = (Comment) picinfo.comments.elementAt(i);
			String[] comment = { commment.getComment(), commment.getTime(),
					commment.getUser() };
			commentList.addElement(comment);
		}

		if (commentList != null && commentList.size() > 0) {
			for (int i = 0; i < commentList.size(); i++) {
				String[] commentArr = (String[]) commentList.elementAt(i);
				CrieLabelField commentLabel = new CrieLabelField("By "
						+ commentArr[2] + " at " + commentArr[1] + ": ",
						MyColor.LIST_DESC_FONT,
						Scale.VIDEO_CONNECT_DETAIL_COMMENT_FONT_HEIGHT
								- (Display.getWidth() > 350 ? 8 : 2),
						LabelField.FOCUSABLE);
				edge = new XYEdges(2, 35 * Display.getWidth() / 480, 2,
						35 * Display.getWidth() / 480);
				commentLabel.isFix = true;
				commentLabel.setMargin(edge);
				// commentLabel.setBorder(BorderFactory.createSimpleBorder(edge,
				// Border.STYLE_TRANSPARENT));
				commentsManager.add(commentLabel);
				commentLabel = new CrieLabelField(commentArr[0], 0x00,
						Scale.VIDEO_CONNECT_DETAIL_COMMENT_FONT_HEIGHT,
						LabelField.FOCUSABLE);
				commentLabel.isFix = true;
				commentLabel.setMargin(edge);
				// commentLabel.setBorder(BorderFactory.createSimpleBorder(edge,
				// Border.STYLE_TRANSPARENT));
				commentsManager.add(commentLabel);

				// postCommentTF.setMaxSize(35);
				// edit.setBorder(BorderFactory.createSimpleBorder(edge,
				// Border.STYLE_TRANSPARENT));

			}
		} else if (commentList.size() == 0) {
			CrieLabelField commentLabel = new CrieLabelField(
					Wording.NO_COMMENT, MyColor.LIST_DESC_FONT,
					Scale.VIDEO_CONNECT_DETAIL_COMMENT_FONT_HEIGHT
							- (Display.getWidth() > 350 ? 8 : 2),
					LabelField.FOCUSABLE);
			edge = new XYEdges(2, 35 * Display.getWidth() / 480, 2,
					35 * Display.getWidth() / 480);
			commentLabel.setMargin(edge);
			commentLabel.isFix = true;
			commentsManager.add(commentLabel);
		}
		FixMainScreen.processHaveComment(commentsManager, picinfo,
				this);
		add(commentsManager);
		// Post Comment Tf
		add(postCommentLabel);
		add(postCommentTF);
		// Submit and backl
		HorizontalFieldManager buttonHorizontalManager = new HorizontalFieldManager(
				HorizontalFieldManager.FIELD_HCENTER
						| HorizontalFieldManager.USE_ALL_WIDTH);

		MyButtonField submitButton = new MyButtonField("Submit",
				ButtonField.ELLIPSIS, true);
		// stopButton.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));

		submitButton.setChangeListener(new ButtonListener(postCommentTF,
				picinfo, 35));
		buttonHorizontalManager.add(submitButton);

		MyButtonField backButton = new MyButtonField("Back",
				ButtonField.ELLIPSIS, true);
		// stopButton.setBorder(BorderFactory.createSimpleBorder(edge,Border.STYLE_TRANSPARENT));
		backButton.setChangeListener(this);
		buttonHorizontalManager.add(backButton);
		edge = new XYEdges(2, 25 * Display.getWidth() / 480, 2, 25 * Display
				.getWidth() / 480);
		buttonHorizontalManager.setMargin(edge);
		add(buttonHorizontalManager);
	}

	public void playerUpdate(Player _player, final String event,
			Object eventData) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				if (event.equals(VOLUME_CHANGED)) {
					// _volumeDisplay.setText("Volume : " +
					// volumeControl.getLevel());
				} else if (event.equals(STARTED)) {
					try {
						if (!isFullScreen) {
							videoControl.setDisplaySize(Const.VIDEO_WIDTH,
									videoControl.getSourceHeight()
											* Const.VIDEO_WIDTH
											/ videoControl.getSourceWidth());
						}
					} catch (MediaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}// edge = new
					Date date = new Date(player.getMediaTime() / 1000);
					SimpleDateFormat df = new SimpleDateFormat("mm:ss");
					_currentTime.setText(df.format(date));
					// _controlButton.setLabel("Pause");
				} else if (event.equals(STOPPED)) {
					Date date = new Date(player.getMediaTime() / 1000);
					SimpleDateFormat df = new SimpleDateFormat("mm:ss");
					_currentTime.setText(df.format(date));
					// _controlButton.setLabel("Start");
				} else if (event.equals(DURATION_UPDATED)) {
					Date date = new Date(player.getDuration() / 1000);
					SimpleDateFormat df = new SimpleDateFormat("mm:ss");
					_duration.setText(df.format(date));
				} else if (event.equals(END_OF_MEDIA)) {
					// _controlButton.setLabel("Start");try{
					try {
						if (player != null
								&& player.getState() == player.STARTED) {
							isFullScreen = false;
							videoControl.setDisplayFullScreen(false);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private class TimerUpdateThread extends Thread {
		private boolean _threadCanRun;

		public void run() {
			_threadCanRun = true;
			while (_threadCanRun) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						if (player != null
								&& player.getState() == Player.STARTED) {
							Date date = new Date(player.getMediaTime() / 1000);
							SimpleDateFormat df = new SimpleDateFormat("mm:ss");
							_currentTime.setText(df.format(date));
						} else {
							_threadCanRun = false;
						}
					}
				});

				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
				}
			}
		}

		public void stop() {
			_threadCanRun = false;
		}
	}
}
