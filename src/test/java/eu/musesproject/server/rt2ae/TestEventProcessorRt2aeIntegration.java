package eu.musesproject.server.rt2ae;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import junit.framework.TestCase;
import eu.musesproject.client.model.JSONIdentifiers;
import eu.musesproject.contextmodel.ContextEvent;
import eu.musesproject.server.contextdatareceiver.JSONManager;
import eu.musesproject.server.contextdatareceiver.UserContextEventDataReceiver;
import eu.musesproject.server.continuousrealtimeeventprocessor.EventProcessor;
import eu.musesproject.server.eventprocessor.correlator.engine.DroolsEngineService;
import eu.musesproject.server.eventprocessor.correlator.global.Rt2aeGlobal;
import eu.musesproject.server.eventprocessor.correlator.model.owl.Event;
import eu.musesproject.server.eventprocessor.impl.EventProcessorImpl;
import eu.musesproject.server.eventprocessor.impl.MusesCorrelationEngineImpl;
import eu.musesproject.server.risktrust.Probability;
import eu.musesproject.server.risktrust.SecurityIncident;
import eu.musesproject.server.risktrust.User;
import eu.musesproject.server.risktrust.UserTrustValue;

public class TestEventProcessorRt2aeIntegration extends TestCase{

	
	private final String defaultSessionId = "DFOOWE423423422H23H";
	private final String testFullCycleWithClues = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1403855894993,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.phone, com.google.process.location, com.google.process.gapps, com.android.bluetooth, com.android.location.fused, com.android.bluetooth, com.google.process.gapps, com.google.process.location, com.google.android.talk, com.google.process.location, com.android.bluetooth, com.android.vending, com.android.systemui, com.android.bluetooth, com.google.android.music:main, com.google.android.inputmethod.latin, com.google.android.music:main, eu.musesproject.client, com.google.process.location, com.google.android.apps.maps:GoogleLocationService, eu.musesproject.client, com.google.process.location, com.android.nfc:handover, system, com.google.process.location, com.google.process.location, com.android.systemui, com.google.process.gapps, com.android.bluetooth, com.android.phone]\",\"appname\":\"Sweden Connectivity\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"unknown\",\"timestamp\":1403854443665,\"bssid\":\"f8:1a:67:83:71:58\",\"bluetoothconnected\":\"TRUE\",\"wifienabled\":\"true\",\"wifineighbors\":\"18\",\"hiddenssid\":\"false\",\"networkid\":\"0\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"1\",\"timestamp\":1403854423397,\"installedapps\":\"Android System,android;com.android.backupconfirm,com.android.backupconfirm;Bluetooth Share,com.android.bluetooth;com.android.browser.provider,com.android.browser.provider;Calculator,com.android.calculator2;Certificate Installer,com.android.certinstaller;Chrome,com.android.chrome;Contacts,com.android.contacts;Package Access Helper,com.android.defcontainer;Basic Daydreams,com.android.dreams.basic;Face Unlock,com.android.facelock;HTML Viewer,com.android.htmlviewer;Input Devices,com.android.inputdevices;Key Chain,com.android.keychain;Launcher,com.android.launcher;Fused Location,com.android.location.fused;MusicFX,com.android.musicfx;Nfc Service,com.android.nfc;Bubbles,com.android.noisefield;Package installer,com.android.packageinstaller;Phase Beam,com.android.phasebeam;Mobile Data,com.android.phone;Search Applications Provider,com.android.providers.applications;Calendar Storage,com.android.providers.calendar;Contacts Storage,com.android.providers.contacts;Download Manager,com.android.providers.downloads;Downloads,com.android.providers.downloads.ui;DRM Protected Content Storage,com.android.providers.drm;Media Storage,com.android.providers.media;com.android.providers.partnerbookmarks,com.android.providers.partnerbookmarks;Settings Storage,com.android.providers.settings;Mobile Network Configuration,com.android.providers.telephony;User Dictionary,com.android.providers.userdictionary;Settings,com.android.settings;com.android.sharedstoragebackup,com.android.sharedstoragebackup;System UI,com.android.systemui;Google Play Store,com.android.vending;VpnDialogs,com.android.vpndialogs;com.android.wallpaper.holospiral,com.android.wallpaper.holospiral;Live Wallpaper Picker,com.android.wallpaper.livepicker;Google Play Books,com.google.android.apps.books;Currents,com.google.android.apps.currents;Google Play Magazines,com.google.android.apps.magazines;Maps,com.google.android.apps.maps;Google+,com.google.android.apps.plus;Picasa Uploader,com.google.android.apps.uploader;Wallet,com.google.android.apps.walletnfcrel;Google Backup Transport,com.google.android.backup;Calendar,com.google.android.calendar;ConfigUpdater,com.google.android.configupdater;Clock,com.google.android.deskclock;Sound Search for Google Play,com.google.android.ears;Email,com.google.android.email;Exchange Services,com.google.android.exchange;Market Feedback Agent,com.google.android.feedback;Gallery,com.google.android.gallery3d;Gmail,com.google.android.gm;Google Play services,com.google.android.gms;Google Search,com.google.android.googlequicksearchbox;Google Services Framework,com.google.android.gsf;Google Account Manager,com.google.android.gsf.login;Google Korean keyboard,com.google.android.inputmethod.korean;Android keyboard,com.google.android.inputmethod.latin;Dictionary Provider,com.google.android.inputmethod.latin.dictionarypack;Google Pinyin,com.google.android.inputmethod.pinyin;Network Location,com.google.android.location;TalkBack,com.google.android.marvin.talkback;Google Play Music,com.google.android.music;Google One Time Init,com.google.android.onetimeinitializer;Google Partner Setup,com.google.android.partnersetup;Setup Wizard,com.google.android.setupwizard;Street View,com.google.android.street;Google Contacts Sync,com.google.android.syncadapters.contacts;Tags,com.google.android.tag;Talk,com.google.android.talk;Google Text-to-speech Engine,com.google.android.tts;Movie Studio,com.google.android.videoeditor;Google Play Movies & TV,com.google.android.videos;com.google.android.voicesearch,com.google.android.voicesearch;YouTube,com.google.android.youtube;Earth,com.google.earth;Quickoffice,com.qo.android.tablet.oem;_MUSES,eu.musesproject.client;Sweden Connectivity,eu.musesproject.musesawareapp;iWnn IME,jp.co.omronsoft.iwnnime.ml;iWnnIME Keyboard (White),jp.co.omronsoft.iwnnime.ml.kbd.white\",\"packagename\":\"\",\"appname\":\"\",\"packagestatus\":\"init\",\"appversion\":\"\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"timestamp\":1403855896071,\"type\":\"open_asset\",\"properties\":{\"resourcePath\":\"/sdcard/Swe/MUSES_partner_grades.txt\",\"resourceName\":\"statistics\",\"resourceType\":\"sensitive\"}},\"requesttype\":\"online_decision\"}";
	
	private final String testSecurityDeviceStateStep1 = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1403855894992,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.phone, com.google.process.location, com.google.process.gapps, com.android.bluetooth, com.android.location.fused, com.android.bluetooth, com.google.process.gapps, com.google.process.location, com.google.android.talk, com.google.process.location, com.android.bluetooth, com.android.vending, com.android.systemui, com.android.bluetooth, com.google.android.music:main, com.google.android.inputmethod.latin, com.google.android.music:main, eu.musesproject.client, com.google.process.location, com.google.android.apps.maps:GoogleLocationService, eu.musesproject.client, com.google.process.location, com.android.nfc:handover, system, com.google.process.location, com.google.process.location, com.android.systemui, com.google.process.gapps, com.android.bluetooth, com.android.phone]\",\"appname\":\"Sweden Connectivity\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"unknown\",\"timestamp\":1403854443665,\"bssid\":\"f8:1a:67:83:71:58\",\"bluetoothconnected\":\"TRUE\",\"wifienabled\":\"true\",\"wifineighbors\":\"18\",\"hiddenssid\":\"false\",\"networkid\":\"0\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"1\",\"timestamp\":1403854423397,\"installedapps\":\"Android System,android;com.android.backupconfirm,com.android.backupconfirm;Bluetooth Share,com.android.bluetooth;com.android.browser.provider,com.android.browser.provider;Calculator,com.android.calculator2;Certificate Installer,com.android.certinstaller;Chrome,com.android.chrome;Contacts,com.android.contacts;Package Access Helper,com.android.defcontainer;Basic Daydreams,com.android.dreams.basic;Face Unlock,com.android.facelock;HTML Viewer,com.android.htmlviewer;Input Devices,com.android.inputdevices;Key Chain,com.android.keychain;Launcher,com.android.launcher;Fused Location,com.android.location.fused;MusicFX,com.android.musicfx;Nfc Service,com.android.nfc;Bubbles,com.android.noisefield;Package installer,com.android.packageinstaller;Phase Beam,com.android.phasebeam;Mobile Data,com.android.phone;Search Applications Provider,com.android.providers.applications;Calendar Storage,com.android.providers.calendar;Contacts Storage,com.android.providers.contacts;Download Manager,com.android.providers.downloads;Downloads,com.android.providers.downloads.ui;DRM Protected Content Storage,com.android.providers.drm;Media Storage,com.android.providers.media;com.android.providers.partnerbookmarks,com.android.providers.partnerbookmarks;Settings Storage,com.android.providers.settings;Mobile Network Configuration,com.android.providers.telephony;User Dictionary,com.android.providers.userdictionary;Settings,com.android.settings;com.android.sharedstoragebackup,com.android.sharedstoragebackup;System UI,com.android.systemui;Google Play Store,com.android.vending;VpnDialogs,com.android.vpndialogs;com.android.wallpaper.holospiral,com.android.wallpaper.holospiral;Live Wallpaper Picker,com.android.wallpaper.livepicker;Google Play Books,com.google.android.apps.books;Currents,com.google.android.apps.currents;Google Play Magazines,com.google.android.apps.magazines;Maps,com.google.android.apps.maps;Google+,com.google.android.apps.plus;Picasa Uploader,com.google.android.apps.uploader;Wallet,com.google.android.apps.walletnfcrel;Google Backup Transport,com.google.android.backup;Calendar,com.google.android.calendar;ConfigUpdater,com.google.android.configupdater;Clock,com.google.android.deskclock;Sound Search for Google Play,com.google.android.ears;Email,com.google.android.email;Exchange Services,com.google.android.exchange;Market Feedback Agent,com.google.android.feedback;Kaspersky Mobile Security, com.kaspersky.mobile.security;Gallery,com.google.android.gallery3d;Gmail,com.google.android.gm;Google Play services,com.google.android.gms;Google Search,com.google.android.googlequicksearchbox;Google Services Framework,com.google.android.gsf;Google Account Manager,com.google.android.gsf.login;Google Korean keyboard,com.google.android.inputmethod.korean;Android keyboard,com.google.android.inputmethod.latin;Dictionary Provider,com.google.android.inputmethod.latin.dictionarypack;Google Pinyin,com.google.android.inputmethod.pinyin;Network Location,com.google.android.location;TalkBack,com.google.android.marvin.talkback;Google Play Music,com.google.android.music;Google One Time Init,com.google.android.onetimeinitializer;Google Partner Setup,com.google.android.partnersetup;Setup Wizard,com.google.android.setupwizard;Street View,com.google.android.street;Google Contacts Sync,com.google.android.syncadapters.contacts;Tags,com.google.android.tag;Talk,com.google.android.talk;Google Text-to-speech Engine,com.google.android.tts;Movie Studio,com.google.android.videoeditor;Google Play Movies & TV,com.google.android.videos;com.google.android.voicesearch,com.google.android.voicesearch;YouTube,com.google.android.youtube;Earth,com.google.earth;Quickoffice,com.qo.android.tablet.oem;_MUSES,eu.musesproject.client;Sweden Connectivity,eu.musesproject.musesawareapp;iWnn IME,jp.co.omronsoft.iwnnime.ml;iWnnIME Keyboard (White),jp.co.omronsoft.iwnnime.ml.kbd.white\",\"packagename\":\"\",\"appname\":\"\",\"packagestatus\":\"init\",\"appversion\":\"\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"timestamp\":1403855896071,\"type\":\"open_asset\",\"properties\":{\"resourcePath\":\"/sdcard/Swe/MUSES_partner_grades.txt\",\"resourceName\":\"statistics\",\"resourceType\":\"insensitive\"}},\"requesttype\":\"online_decision\"}";
	private final String testSecurityDeviceStateStep2 = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1403855894993,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.phone, com.google.process.location, com.google.process.gapps, com.android.bluetooth, com.android.location.fused, com.android.bluetooth, com.google.process.gapps, com.google.process.location, com.google.android.talk, com.google.process.location, com.android.bluetooth, com.android.vending, com.android.systemui, com.android.bluetooth, com.google.android.music:main, com.google.android.inputmethod.latin, com.google.android.music:main, eu.musesproject.client, com.google.process.location, com.google.android.apps.maps:GoogleLocationService, eu.musesproject.client, com.google.process.location, com.android.nfc:handover, system, com.google.process.location, com.google.process.location, com.android.systemui, com.google.process.gapps, com.android.bluetooth, com.android.phone]\",\"appname\":\"Sweden Connectivity\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"unknown\",\"timestamp\":1403854443665,\"bssid\":\"f8:1a:67:83:71:58\",\"bluetoothconnected\":\"TRUE\",\"wifienabled\":\"true\",\"wifineighbors\":\"18\",\"hiddenssid\":\"false\",\"networkid\":\"0\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"1\",\"timestamp\":1403854423397,\"installedapps\":\"Android System,android;com.android.backupconfirm,com.android.backupconfirm;Bluetooth Share,com.android.bluetooth;com.android.browser.provider,com.android.browser.provider;Calculator,com.android.calculator2;Certificate Installer,com.android.certinstaller;Chrome,com.android.chrome;Contacts,com.android.contacts;Package Access Helper,com.android.defcontainer;Basic Daydreams,com.android.dreams.basic;Face Unlock,com.android.facelock;HTML Viewer,com.android.htmlviewer;Input Devices,com.android.inputdevices;Key Chain,com.android.keychain;Launcher,com.android.launcher;Fused Location,com.android.location.fused;MusicFX,com.android.musicfx;Nfc Service,com.android.nfc;Bubbles,com.android.noisefield;Package installer,com.android.packageinstaller;Phase Beam,com.android.phasebeam;Mobile Data,com.android.phone;Search Applications Provider,com.android.providers.applications;Calendar Storage,com.android.providers.calendar;Contacts Storage,com.android.providers.contacts;Download Manager,com.android.providers.downloads;Downloads,com.android.providers.downloads.ui;DRM Protected Content Storage,com.android.providers.drm;Media Storage,com.android.providers.media;com.android.providers.partnerbookmarks,com.android.providers.partnerbookmarks;Settings Storage,com.android.providers.settings;Mobile Network Configuration,com.android.providers.telephony;User Dictionary,com.android.providers.userdictionary;Settings,com.android.settings;com.android.sharedstoragebackup,com.android.sharedstoragebackup;System UI,com.android.systemui;Google Play Store,com.android.vending;VpnDialogs,com.android.vpndialogs;com.android.wallpaper.holospiral,com.android.wallpaper.holospiral;Live Wallpaper Picker,com.android.wallpaper.livepicker;Google Play Books,com.google.android.apps.books;Currents,com.google.android.apps.currents;Google Play Magazines,com.google.android.apps.magazines;Maps,com.google.android.apps.maps;Google+,com.google.android.apps.plus;Picasa Uploader,com.google.android.apps.uploader;Wallet,com.google.android.apps.walletnfcrel;Google Backup Transport,com.google.android.backup;Calendar,com.google.android.calendar;ConfigUpdater,com.google.android.configupdater;Clock,com.google.android.deskclock;Sound Search for Google Play,com.google.android.ears;Email,com.google.android.email;Exchange Services,com.google.android.exchange;Market Feedback Agent,com.google.android.feedback;Gallery,com.google.android.gallery3d;Gmail,com.google.android.gm;Google Play services,com.google.android.gms;Google Search,com.google.android.googlequicksearchbox;Google Services Framework,com.google.android.gsf;Google Account Manager,com.google.android.gsf.login;Google Korean keyboard,com.google.android.inputmethod.korean;Android keyboard,com.google.android.inputmethod.latin;Dictionary Provider,com.google.android.inputmethod.latin.dictionarypack;Google Pinyin,com.google.android.inputmethod.pinyin;Network Location,com.google.android.location;TalkBack,com.google.android.marvin.talkback;Google Play Music,com.google.android.music;Google One Time Init,com.google.android.onetimeinitializer;Google Partner Setup,com.google.android.partnersetup;Setup Wizard,com.google.android.setupwizard;Street View,com.google.android.street;Google Contacts Sync,com.google.android.syncadapters.contacts;Tags,com.google.android.tag;Talk,com.google.android.talk;Google Text-to-speech Engine,com.google.android.tts;Movie Studio,com.google.android.videoeditor;Google Play Movies & TV,com.google.android.videos;com.google.android.voicesearch,com.google.android.voicesearch;YouTube,com.google.android.youtube;Earth,com.google.earth;Quickoffice,com.qo.android.tablet.oem;_MUSES,eu.musesproject.client;Sweden Connectivity,eu.musesproject.musesawareapp;iWnn IME,jp.co.omronsoft.iwnnime.ml;iWnnIME Keyboard (White),jp.co.omronsoft.iwnnime.ml.kbd.white\",\"packagename\":\"\",\"appname\":\"\",\"packagestatus\":\"init\",\"appversion\":\"\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"timestamp\":1403855896071,\"type\":\"open_asset\",\"properties\":{\"resourcePath\":\"/sdcard/Swe/MUSES_partner_grades.txt\",\"resourceName\":\"statistics\",\"resourceType\":\"insensitive\"}},\"requesttype\":\"online_decision\"}";
	
	private final String testUserAction = "{\"behavior\":{\"action\":\"cancel\"},\"requesttype\":\"user_behavior\"}";
	
	
	private final String testOpenConfAssetInSecure = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1401986291588,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.server.device.enterprise:remote, com.android.phone, com.google.process.gapps, com.google.android.gms.drive, com.android.smspush, com.samsung.music, system, com.sec.spp.push, com.google.android.talk, com.google.process.location, com.android.systemui, com.google.android.gms, com.google.android.apps.maps, com.android.phone, com.tgrape.android.radar, com.android.phone, com.samsung.music, com.android.systemui, com.wssnps, com.google.android.googlequicksearchbox:search, com.android.settings, com.sec.android.app.twdvfs, com.android.bluetooth, com.google.process.location, com.sec.android.inputmethod, com.google.android.youtube, com.android.defcontainer, android.process.media, com.google.android.gms, com.sec.phone, com.sec.msc.learninghub, com.google.process.gapps, com.sec.factory, com.google.process.location, com.android.server.vpn.enterprise:remote, com.android.phone, com.sec.android.widgetapp.at.hero.accuweather.widget:remote, eu.musesproject.client, com.android.MtpApplication, com.vlingo.midas, com.google.process.gapps, com.google.android.gms, eu.musesproject.client, com.android.phone, net.openvpn.openvpn, com.android.phone, system, com.sec.android.app.sysscope, com.google.process.location, com.google.process.location, com.samsung.videohub, com.google.android.tts, com.sec.android.app.videoplayer, com.google.android.gms, com.google.process.gapps]\",\"appname\":\"Sweden Connectivity\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"WEP\",\"timestamp\":1401986235742,\"bssid\":\"24:a4:3c:04:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"6\",\"hiddenssid\":\"false\",\"networkid\":\"1\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"}},\"action\":{\"timestamp\":1401986354214,\"type\":\"open_asset\",\"properties\":{\"resourcePath\":\"/sdcard/Swe/MUSES_partner_grades.txt\",\"resourceName\":\"statistics\",\"resourceType\":\"sensitive\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testOpenConfAssetSecure = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1401986291588,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.server.device.enterprise:remote, com.android.phone, com.google.process.gapps, com.google.android.gms.drive, com.android.smspush, com.samsung.music, system, com.sec.spp.push, com.google.android.talk, com.google.process.location, com.android.systemui, com.google.android.gms, com.google.android.apps.maps, com.android.phone, com.tgrape.android.radar, com.android.phone, com.samsung.music, com.android.systemui, com.wssnps, com.google.android.googlequicksearchbox:search, com.android.settings, com.sec.android.app.twdvfs, com.android.bluetooth, com.google.process.location, com.sec.android.inputmethod, com.google.android.youtube, com.android.defcontainer, android.process.media, com.google.android.gms, com.sec.phone, com.sec.msc.learninghub, com.google.process.gapps, com.sec.factory, com.google.process.location, com.android.server.vpn.enterprise:remote, com.android.phone, com.sec.android.widgetapp.at.hero.accuweather.widget:remote, eu.musesproject.client, com.android.MtpApplication, com.vlingo.midas, com.google.process.gapps, com.google.android.gms, eu.musesproject.client, com.android.phone, net.openvpn.openvpn, com.android.phone, system, com.sec.android.app.sysscope, com.google.process.location, com.google.process.location, com.samsung.videohub, com.google.android.tts, com.sec.android.app.videoplayer, com.google.android.gms, com.google.process.gapps]\",\"appname\":\"Sweden Connectivity\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"WPA2\",\"timestamp\":1401986235742,\"bssid\":\"24:a4:3c:04:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"6\",\"hiddenssid\":\"false\",\"networkid\":\"1\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"}},\"action\":{\"timestamp\":1401986354214,\"type\":\"open_asset\",\"properties\":{\"resourcePath\":\"/sdcard/Swe/MUSES_partner_grades.txt\",\"resourceName\":\"statistics\",\"resourceType\":\"sensitive\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testBlacklistApp = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1402313215730,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.server.device.enterprise:remote, com.android.phone, com.google.process.gapps, com.google.android.gms.drive, com.android.smspush, com.samsung.music, system, com.sec.spp.push, com.google.android.talk, com.google.process.location, com.android.systemui, com.google.android.gms, com.google.android.apps.maps, com.android.phone, com.sec.android.app.controlpanel, com.tgrape.android.radar, com.android.phone, com.samsung.music, com.android.systemui, com.wssnps, com.google.android.googlequicksearchbox:search, com.android.settings, com.sec.android.app.twdvfs, com.android.bluetooth, com.google.process.location, com.sec.android.inputmethod, com.google.android.youtube, android.process.media, com.google.android.gms, com.sec.phone, com.sec.msc.learninghub, com.google.process.gapps, com.sec.factory, com.google.process.location, com.android.server.vpn.enterprise:remote, com.android.phone, com.sec.android.widgetapp.at.hero.accuweather.widget:remote, eu.musesproject.client, com.android.MtpApplication, com.vlingo.midas, com.google.process.gapps, com.google.android.gms, eu.musesproject.client, com.android.phone, net.openvpn.openvpn, com.android.phone, system, com.sec.android.app.sysscope, com.google.process.location, com.google.process.location, com.samsung.videohub, com.google.android.tts, com.google.android.gm, com.sec.android.app.videoplayer, com.google.android.gms, com.google.process.gapps]\",\"appname\":\"Gmail\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"[WPA2-PSK-TKIP+CCMP][ESS]\",\"timestamp\":1402313210321,\"bssid\":\"24:a4:3c:03:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"8\",\"hiddenssid\":\"false\",\"networkid\":\"1\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"}},\"action\":{\"timestamp\":1402313215730,\"type\":\"open_application\",\"properties\":{\"package\":\"\",\"appname\":\"Gmail\",\"version\":\"\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testNotBlacklistApp = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1402313215730,\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.server.device.enterprise:remote, com.android.phone, com.google.process.gapps, com.google.android.gms.drive, com.android.smspush, com.samsung.music, system, com.sec.spp.push, com.google.android.talk, com.google.process.location, com.android.systemui, com.google.android.gms, com.google.android.apps.maps, com.android.phone, com.sec.android.app.controlpanel, com.tgrape.android.radar, com.android.phone, com.samsung.music, com.android.systemui, com.wssnps, com.google.android.googlequicksearchbox:search, com.android.settings, com.sec.android.app.twdvfs, com.android.bluetooth, com.google.process.location, com.sec.android.inputmethod, com.google.android.youtube, android.process.media, com.google.android.gms, com.sec.phone, com.sec.msc.learninghub, com.google.process.gapps, com.sec.factory, com.google.process.location, com.android.server.vpn.enterprise:remote, com.android.phone, com.sec.android.widgetapp.at.hero.accuweather.widget:remote, eu.musesproject.client, com.android.MtpApplication, com.vlingo.midas, com.google.process.gapps, com.google.android.gms, eu.musesproject.client, com.android.phone, net.openvpn.openvpn, com.android.phone, system, com.sec.android.app.sysscope, com.google.process.location, com.google.process.location, com.samsung.videohub, com.google.android.tts, com.google.android.gm, com.sec.android.app.videoplayer, com.google.android.gms, com.google.process.gapps]\",\"appname\":\"Other\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"[WPA2-PSK-TKIP+CCMP][ESS]\",\"timestamp\":1402313210321,\"bssid\":\"24:a4:3c:03:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"8\",\"hiddenssid\":\"false\",\"networkid\":\"1\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"}},\"action\":{\"timestamp\":1402313215730,\"type\":\"open_application\",\"properties\":{\"package\":\"\",\"appname\":\"Other\",\"version\":\"\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testEmailWithoutAttachments = "{\"sensor\":{},\"action\":{\"type\":\"ACTION_SEND_MAIL\",\"timestamp\" : \"1389885147\",\"properties\": {\"from\":\"max.mustermann@generic.com\",\"to\":\"the.reiceiver@generic.com, another.direct.receiver@generic.com\",\"cc\":\"other.listener@generic.com, 2other.listener@generic.com\",\"bcc\":\"hidden.reiceiver@generic.com\",\"subject\":\"MUSES sensor status subject\",\"noAttachments\" : 0,\"attachmentInfo\": \"\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testEmailWithAttachments = "{\"sensor\":{},\"action\":{\"type\":\"ACTION_SEND_MAIL\",\"timestamp\" : \"1389885147\",\"properties\": {\"from\":\"max.mustermann@generic.com\",\"to\":\"the.reiceiver@generic.com, another.direct.receiver@generic.com\",\"cc\":\"other.listener@generic.com, 2other.listener@generic.com\",\"bcc\":\"hidden.reiceiver@generic.com\",\"subject\":\"MUSES sensor status subject\",\"noAttachments\" : 2,\"attachmentInfo\": \"name,type,size;name2,type2,size2\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testEmailWithAttachmentsReal = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"2\",\"timestamp\":1408434038945,\"appversion\":\"1\",\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.server.device.enterprise:remote, com.android.phone, com.google.process.gapps, com.google.android.gms.drive, com.android.smspush, system, com.sec.spp.push, com.google.android.talk, com.android.systemui, com.google.android.music:main, com.google.android.gms.wearable, com.google.android.gms, com.android.phone, com.tgrape.android.radar, com.android.phone, com.android.systemui, com.google.android.googlequicksearchbox:search, com.android.settings, com.sec.android.app.twdvfs, com.android.bluetooth, com.google.process.location, com.sec.android.inputmethod, com.android.defcontainer, android.process.media, com.sec.phone, com.sec.msc.learninghub, com.google.process.gapps, com.sec.factory, com.google.process.location, com.android.server.vpn.enterprise:remote, com.fermax.fermaxapp, com.android.phone, com.wssyncmldm, com.google.android.music:main, com.sec.android.widgetapp.at.hero.accuweather.widget:remote, eu.musesproject.client, com.android.MtpApplication, com.vlingo.midas, com.google.process.gapps, eu.musesproject.client, com.android.phone, com.android.phone, system, com.sec.android.app.sysscope, com.google.process.gapps, com.google.process.location, com.google.process.location, com.sec.android.app.videoplayer, com.google.process.gapps, android.process.media]\",\"appname\":\"Sweden Connectivity\",\"packagename\":\"eu.musesproject.musesawareapp\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"[WPA2-PSK-TKIP+CCMP][ESS]\",\"timestamp\":1408434029656,\"bssid\":\"24:a4:3c:04:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"6\",\"hiddenssid\":\"false\",\"networkid\":\"1\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"1\",\"timestamp\":1408433959585,\"installedapps\":\"PS Touch,air.com.adobe.pstouch.oem1,1004002;Sistema Android,android,16;Busqueda de Google,android.googleSearch.googleSearchWidget,1;Crayon physics,com.acrodea.crayonphysics,1;Aplicacion MTP,com.android.MtpApplication,1;Preconfig,com.android.Preconfig,16;com.android.backupconfirm,com.android.backupconfirm,16;Compartir Bluetooth,com.android.bluetooth,16;Internet,com.android.browser,16;Calendario,com.android.calendar,16;Instalador de certificados,com.android.certinstaller,16;Chrome,com.android.chrome,1985131;TestService,com.android.clipboardsaveservice,1;Contactos,com.android.contacts,16;Ayudante acceso a paquete,com.android.defcontainer,16;Correo electronico,com.android.email,410000;Servicios de Exchange,com.android.exchange,410000;Desbloqueo facial,com.android.facelock,16;Visor de HTML,com.android.htmlviewer,16;Input Devices,com.android.inputdevices,16;Key Chain,com.android.keychain,16;MusicFX,com.android.musicfx,10400;Bubbles,com.android.noisefield,1;Instalador de paquete,com.android.packageinstaller,16;Phase beam,com.android.phasebeam,1;Telefono,com.android.phone,16;PickupTutorial,com.android.pickuptutorial,16;Proveedor aplicaciones busqueda,com.android.providers.applications,16;Almacenamiento de calendario,com.android.providers.calendar,16;Informacion de los contactos,com.android.providers.contacts,16;Descargas,com.android.providers.downloads,16;Descargas,com.android.providers.downloads.ui,16;Almacenamiento de contenido protegido por DRM,com.android.providers.drm,16;Almacenamiento de medios,com.android.providers.media,513;Almacen. de seguridad,com.android.providers.security,16;Almacenamiento de ajustes,com.android.providers.settings,16;Configuracion de red movil,com.android.providers.telephony,16;User Dictionary,com.android.providers.userdictionary,16;Enterprise SysScope Service,com.android.server.device.enterprise,16;Enterprise VPN Services,com.android.server.vpn.enterprise,16;Ajustes,com.android.settings,16;Rastreador movil,com.android.settings.mt,1;com.android.sharedstoragebackup,com.android.sharedstoragebackup,16;com.android.smspush,com.android.smspush,16;Grabadora de sonidos,com.android.soundrecorder,16;Kit herramientas SIM,com.android.stk,16;IU sistema,com.android.systemui,16;Google Play Store,com.android.vending,80290013;VpnDialogs,com.android.vpndialogs,16;Selector de fondos de pantalla en movimiento,com.android.wallpaper.livepicker,16;Paper Artist,com.dama.paperartist,1002043;Popup Note,com.diotek.mini_penmemo,6074;Dropbox,com.dropbox.android,240200;SipDemo,com.example.android.sip,0;Fermax,com.fermax.fermaxapp,1;FermaxappTestTest,com.fermax.fermaxapp.test,1;Controles remotos,com.fmm.dm,2;Controles remotos,com.fmm.ds,1;Google Play Books,com.google.android.apps.books,30149;Google Play Kiosco,com.google.android.apps.magazines,2014051213;Maps,com.google.android.apps.maps,802003401;Google+,com.google.android.apps.plus,413153783;Picasa Uploader,com.google.android.apps.uploader,40000;Google Backup Transport,com.google.android.backup,16;Agente comentarios Market,com.google.android.feedback,16;Gmail,com.google.android.gm,4900120;Servicios de Google Play,com.google.android.gms,5089032;Busqueda de Google,com.google.android.googlequicksearchbox,300305160;Marco de servicios de Google,com.google.android.gsf,16;Administrador de cuentas de Google,com.google.android.gsf.login,16;Ubicacion de red,com.google.android.location,1110;TalkBack,com.google.android.marvin.talkback,107;Google Play Music,com.google.android.music,1617;Configuracion para partners de Google,com.google.android.partnersetup,16;Google Play Games,com.google.android.play.games,20110032;Asistente de configuracion,com.google.android.setupwizard,130;Street View,com.google.android.street,18102;Sincronizacion de Google Bookmarks,com.google.android.syncadapters.bookmarks,16;Sincronizacion de Google Calendar,com.google.android.syncadapters.calendar,16;Sincronizacion de contactos de Google,com.google.android.syncadapters.contacts,16;Hangouts,com.google.android.talk,21317130;Sintesis de Google,com.google.android.tts,210030103;Google Play Movies,com.google.android.videos,32251;com.google.android.voicesearch,com.google.android.voicesearch,4000000;YouTube,com.google.android.youtube,5741;Aurora 2,com.hu1.wallpaper.aurora2,1;Polaris Office,com.infraware.PolarisOfficeStdForTablet,2077500580;Recortar,com.lifevibes.trimapp,1;ChocoEUKor,com.monotype.android.font.chococooky,1;CoolEUKor,com.monotype.android.font.cooljazz,1;Helv Neue S,com.monotype.android.font.helvneuelt,1;RoseEUKor,com.monotype.android.font.rosemary,1;CatLog,com.nolanlawson.logcat,42;Fermax example,com.okode.linphone,1;Samsung account,com.osp.app.signin,140266;Peel Smart Remote,com.peel.app,30342;VideoStreaming,com.s2.videostreaming,1;SpaceIT,com.s2grupo.spaceit,1;Samsung TTS,com.samsung.SMT,0;Ajustes de licencia,com.samsung.android.app.divx,0;Deep sea,com.samsung.android.livewallpaper.deepsea,1;Luminous dots,com.samsung.android.livewallpaper.luminousdots,1;com.samsung.app.playreadyui,com.samsung.app.playreadyui,1;AvrcpServiceSamsung,com.samsung.avrcp,16;Reproduccion de grupo,com.samsung.groupcast,1005058;Ayuda,com.samsung.helphub,1;Music Hub,com.samsung.music,1;INDIServiceManager,com.samsung.scrc.idi.server,2290904;CSC,com.samsung.sec.android.application.csc,16;Idea Sketch,com.samsung.sec.sketch,2;ShareShotService,com.samsung.shareshot,1;Gmail,com.samsung.spell.gmail,1;Maps,com.samsung.spell.googlemaps,1;Busqueda de Google,com.samsung.spell.googlesearch,1;Topic Wall,com.samsung.topicwall,1;Video Hub,com.samsung.videohub,1193;Ajustes USB,com.sec.android.Kies,1;AllShare Service,com.sec.android.allshare.framework,10;DataCreate,com.sec.android.app.DataCreate,1;Wi-Fi Direct,com.sec.android.app.FileShareClient,1;Uso compartido de Wi-Fi Direct,com.sec.android.app.FileShareServer,1;SecSetupWizard,com.sec.android.app.SecSetupWizard,1;com.sec.android.app.SuggestionService,com.sec.android.app.SuggestionService,1;BluetoothTest,com.sec.android.app.bluetoothtest,1;Camara,com.sec.android.app.camera,1;Alarma,com.sec.android.app.clockpackage,1;Administrador de tareas,com.sec.android.app.controlpanel,1;Factory Mode,com.sec.android.app.factorymode,1;Game Hub,com.sec.android.app.gamehub,13010801;Kies mediante Wi-Fi,com.sec.android.app.kieswifi,2;Inicio TouchWiz,com.sec.android.app.launcher,16;Lcdtest,com.sec.android.app.lcdtest,1;com.sec.android.app.minimode.res,com.sec.android.app.minimode.res,16;Impresion movil,com.sec.android.app.mobileprint,21;Reproductor de musica,com.sec.android.app.music,1;Mis archivos,com.sec.android.app.myfiles,1;Perso,com.sec.android.app.personalization,16;PhoneUtil,com.sec.android.app.phoneutil,1;Calculadora,com.sec.android.app.popupcalculator,1;PopupuiReceiver,com.sec.android.app.popupuireceiver,1;Samsung Apps,com.sec.android.app.samsungapps,4700060;SamsungAppsUNA2,com.sec.android.app.samsungapps.una2,2035;Self Test Mode,com.sec.android.app.selftestmode,1;Service mode,com.sec.android.app.servicemodeapp,16;Nota S,com.sec.android.app.snotebook,1309093781;SNS,com.sec.android.app.sns3,10;SurfSetProp,com.sec.android.app.surfsetprop,1;SysScope,com.sec.android.app.sysscope,4;TwDVFSApp,com.sec.android.app.twdvfs,1;Editor de video,com.sec.android.app.ve,4;Reproductor de video,com.sec.android.app.videoplayer,1;SecWallpaperChooser,com.sec.android.app.wallpaperchooser,16;com.sec.android.app.wfdbroker,com.sec.android.app.wfdbroker,100;WlanTest,com.sec.android.app.wlantest,1;Reloj mundial,com.sec.android.app.worldclock,1;CloudAgent,com.sec.android.cloudagent,1;Dropbox,com.sec.android.cloudagent.dropboxoobe,1;ContextAwareService,com.sec.android.contextaware,16;Weather Daemon,com.sec.android.daemonapp.ap.accuweather,1;News Daemon(EUR),com.sec.android.daemonapp.ap.yahoonews,1;Yahoo! Finance Daemon,com.sec.android.daemonapp.ap.yahoostock.stockclock,1;DirectShareManager,com.sec.android.directshare,2;OmaDrmPopup,com.sec.android.drmpopup,1;Actualizac de software,com.sec.android.fotaclient,1;AllShare Cast Dongle S\\/W Update,com.sec.android.fwupgrade,1800011;Galeria,com.sec.android.gallery3d,30682;Comando rapido,com.sec.android.gesturepad,1;Teclado Samsung,com.sec.android.inputmethod,1;Pruebe el desplazamiento,com.sec.android.motions.settings.panningtutorial,1;Dispositivos cercanos,com.sec.android.nearby.mediaserver,16;Application installer,com.sec.android.preloadinstaller,1;BadgeProvider,com.sec.android.provider.badge,1;LogsProvider,com.sec.android.provider.logsprovider,16;Nota S,com.sec.android.provider.snote,1304012187;com.sec.android.providers.downloads,com.sec.android.providers.downloads,16;Copia de seg. y restaur.,com.sec.android.sCloudBackupApp,131;Samsung Backup Provider,com.sec.android.sCloudBackupProvider,14;Samsung Cloud Data Relay,com.sec.android.sCloudRelayData,201008;Samsung Syncadapters,com.sec.android.sCloudSync,269;Samsung Browser SyncAdapter,com.sec.android.sCloudSyncBrowser,1;Samsung Calendar SyncAdapter,com.sec.android.sCloudSyncCalendar,1;Samsung Contact SyncAdapter,com.sec.android.sCloudSyncContacts,1;Samsung SNote SyncAdapter,com.sec.android.sCloudSyncSNote,1;SASlideShow,com.sec.android.saslideshow,1;CapabilityManagerService,com.sec.android.service.cm,2;Ajustes,com.sec.android.signaturelock,1;Widget de Planificador S,com.sec.android.widgetapp.SPlannerAppWidget,1;AllShare Cast,com.sec.android.widgetapp.allsharecast,1;Reloj (moderno),com.sec.android.widgetapp.analogclocksimple,1;Yahoo! News,com.sec.android.widgetapp.ap.yahoonews,1;Weather Widget Main,com.sec.android.widgetapp.at.hero.accuweather,1;Weather Widget,com.sec.android.widgetapp.at.hero.accuweather.widget,1;Yahoo! Finance,com.sec.android.widgetapp.at.yahoostock.stockclock,1;Reloj digital,com.sec.android.widgetapp.digitalclock,1;Reloj dual (analogico),com.sec.android.widgetapp.dualclockanalog,1;Reloj dual (digital),com.sec.android.widgetapp.dualclockdigital,1;Monitor de aplicaciones,com.sec.android.widgetapp.programmonitorwidget,1;Manual de usuario,com.sec.android.widgetapp.webmanual,1;Error,com.sec.app.RilErrorNotifier,1;com.sec.bcservice,com.sec.bcservice,1;ChatON,com.sec.chaton,300450243;DSMForwarding,com.sec.dsm.phone,16;DSMLawmo,com.sec.dsm.system,1;EnterprisePermissions,com.sec.enterprise.permissions,1;Factory Test,com.sec.factory,1;MiniTaskcloserService,com.sec.minimode.taskcloser,1;Learning Hub,com.sec.msc.learninghub,13072501;AllShare Play,com.sec.pcw,3302;Remote Controls,com.sec.pcw.device,40;com.sec.phone,com.sec.phone,1;FlashAnnotate,com.sec.spen.flashannotate,1;FlashAnnotateSvc,com.sec.spen.flashannotatesvc,1;Samsung Push Service,com.sec.spp.push,91;Muro de fotos,com.siso.photoWall,1;SyncmlDS,com.smlds,1;Explorer,com.speedsoftware.explorer,34;S Suggest,com.tgrape.android.radar,3286;Ping & DNS,com.ulfdittmer.android.ping,79;Resource Manager,com.visionobjects.resourcemanager,1;S Voice,com.vlingo.midas,1000;OMACP,com.wsomacp,4;wssyncmlnps,com.wssnps,2;Actualizacion de software,com.wssyncmldm,2;OpenVPN Remote,de.blinkt.openvpn.remote,0;_MUSES,eu.musesproject.client,1;Sweden Connectivity,eu.musesproject.musesawareapp,1;MusesAwareAppTestTest,eu.musesproject.musesawareapp.test,1;OpenVPN Connect,net.openvpn.openvpn,56;Alfresco,org.alfresco.mobile.android.application,30;SmartcardService,org.simalliance.openmobileapi.service,2;VLC,org.videolan.vlc.betav7neon,9800\",\"packagename\":\"init\",\"appname\":\"init\",\"packagestatus\":\"init\",\"appversion\":\"-1\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"type\":\"ACTION_SEND_MAIL\",\"timestamp\":1408434044686,\"properties\":{\"bcc\":\"hidden.reiceiver@generic.com\",\"to\":\"the.reiceiver@generic.com, another.direct.receiver@generic.com\",\"noAttachments\":\"1\",\"attachmentInfo\":\"pdf\",\"from\":\"max.mustermann@generic.com\",\"subject\":\"MUSES sensor status subject\",\"cc\":\"other.listener@generic.com, 2other.listener@generic.com\"}},\"username\":\"muses\",\"device_id\":\"9aa326e4fd9ccf61\",\"requesttype\":\"online_decision\"}";
	private final String testVirusFound = "{\"sensor\":{},\"action\":{\"type\":\"virus_found\",\"timestamp\" : \"1389885147\",\"properties\": {\"path\":\"/sdcard/Swe/virus.txt\",\"name\":\"seriour_virus\",\"severity\":\"high\"}},\"requesttype\":\"online_decision\",\"device_id\":\"36474929437562939\",\"username\":\"muses\"}";
	private final String testVirusFoundReal = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1408434702014,\"appversion\":\"1\",\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.server.device.enterprise:remote, com.android.phone, com.google.process.gapps, com.google.android.gms.drive, com.android.smspush, system, com.sec.spp.push, com.google.android.talk, com.android.systemui, com.google.android.music:main, com.google.android.gms.wearable, com.google.android.gms, com.android.phone, com.tgrape.android.radar, com.android.phone, com.android.systemui, com.google.android.googlequicksearchbox:search, com.android.settings, com.sec.android.app.twdvfs, com.android.bluetooth, com.google.process.location, com.sec.android.inputmethod, com.android.defcontainer, com.sec.phone, com.sec.msc.learninghub, com.google.process.gapps, com.sec.factory, com.google.process.location, com.android.server.vpn.enterprise:remote, com.fermax.fermaxapp, com.android.phone, com.wssyncmldm, com.google.android.music:main, com.sec.android.widgetapp.at.hero.accuweather.widget:remote, eu.musesproject.client, com.vlingo.midas, com.google.process.gapps, eu.musesproject.client, com.android.phone, com.android.phone, system, com.sec.android.app.sysscope, com.google.process.gapps, com.google.process.location, com.google.process.location, com.sec.android.app.videoplayer, com.google.process.gapps, android.process.media]\",\"appname\":\"Sweden Connectivity\",\"packagename\":\"eu.musesproject.musesawareapp\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"wifiencryption\":\"[WPA2-PSK-TKIP+CCMP][ESS]\",\"timestamp\":1408434690992,\"bssid\":\"24:a4:3c:04:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"10\",\"hiddenssid\":\"false\",\"networkid\":\"1\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"1\",\"timestamp\":1408433959585,\"installedapps\":\"PS Touch,air.com.adobe.pstouch.oem1,1004002;Sistema Android,android,16;Busqueda de Google,android.googleSearch.googleSearchWidget,1;Crayon physics,com.acrodea.crayonphysics,1;Aplicacion MTP,com.android.MtpApplication,1;Preconfig,com.android.Preconfig,16;com.android.backupconfirm,com.android.backupconfirm,16;Compartir Bluetooth,com.android.bluetooth,16;Internet,com.android.browser,16;Calendario,com.android.calendar,16;Instalador de certificados,com.android.certinstaller,16;Chrome,com.android.chrome,1985131;TestService,com.android.clipboardsaveservice,1;Contactos,com.android.contacts,16;Ayudante acceso a paquete,com.android.defcontainer,16;Correo electronico,com.android.email,410000;Servicios de Exchange,com.android.exchange,410000;Desbloqueo facial,com.android.facelock,16;Visor de HTML,com.android.htmlviewer,16;Input Devices,com.android.inputdevices,16;Key Chain,com.android.keychain,16;MusicFX,com.android.musicfx,10400;Bubbles,com.android.noisefield,1;Instalador de paquete,com.android.packageinstaller,16;Phase beam,com.android.phasebeam,1;Telefono,com.android.phone,16;PickupTutorial,com.android.pickuptutorial,16;Proveedor aplicaciones busqueda,com.android.providers.applications,16;Almacenamiento de calendario,com.android.providers.calendar,16;Informacion de los contactos,com.android.providers.contacts,16;Descargas,com.android.providers.downloads,16;Descargas,com.android.providers.downloads.ui,16;Almacenamiento de contenido protegido por DRM,com.android.providers.drm,16;Almacenamiento de medios,com.android.providers.media,513;Almacen. de seguridad,com.android.providers.security,16;Almacenamiento de ajustes,com.android.providers.settings,16;Configuracion de red movil,com.android.providers.telephony,16;User Dictionary,com.android.providers.userdictionary,16;Enterprise SysScope Service,com.android.server.device.enterprise,16;Enterprise VPN Services,com.android.server.vpn.enterprise,16;Ajustes,com.android.settings,16;Rastreador movil,com.android.settings.mt,1;com.android.sharedstoragebackup,com.android.sharedstoragebackup,16;com.android.smspush,com.android.smspush,16;Grabadora de sonidos,com.android.soundrecorder,16;Kit herramientas SIM,com.android.stk,16;IU sistema,com.android.systemui,16;Google Play Store,com.android.vending,80290013;VpnDialogs,com.android.vpndialogs,16;Selector de fondos de pantalla en movimiento,com.android.wallpaper.livepicker,16;Paper Artist,com.dama.paperartist,1002043;Popup Note,com.diotek.mini_penmemo,6074;Dropbox,com.dropbox.android,240200;SipDemo,com.example.android.sip,0;Fermax,com.fermax.fermaxapp,1;FermaxappTestTest,com.fermax.fermaxapp.test,1;Controles remotos,com.fmm.dm,2;Controles remotos,com.fmm.ds,1;Google Play Books,com.google.android.apps.books,30149;Google Play Kiosco,com.google.android.apps.magazines,2014051213;Maps,com.google.android.apps.maps,802003401;Google+,com.google.android.apps.plus,413153783;Picasa Uploader,com.google.android.apps.uploader,40000;Google Backup Transport,com.google.android.backup,16;Agente comentarios Market,com.google.android.feedback,16;Gmail,com.google.android.gm,4900120;Servicios de Google Play,com.google.android.gms,5089032;Busqueda de Google,com.google.android.googlequicksearchbox,300305160;Marco de servicios de Google,com.google.android.gsf,16;Administrador de cuentas de Google,com.google.android.gsf.login,16;Ubicacion de red,com.google.android.location,1110;TalkBack,com.google.android.marvin.talkback,107;Google Play Music,com.google.android.music,1617;Configuracion para partners de Google,com.google.android.partnersetup,16;Google Play Games,com.google.android.play.games,20110032;Asistente de configuracion,com.google.android.setupwizard,130;Street View,com.google.android.street,18102;Sincronizacion de Google Bookmarks,com.google.android.syncadapters.bookmarks,16;Sincronizacion de Google Calendar,com.google.android.syncadapters.calendar,16;Sincronizacion de contactos de Google,com.google.android.syncadapters.contacts,16;Hangouts,com.google.android.talk,21317130;Sintesis de Google,com.google.android.tts,210030103;Google Play Movies,com.google.android.videos,32251;com.google.android.voicesearch,com.google.android.voicesearch,4000000;YouTube,com.google.android.youtube,5741;Aurora 2,com.hu1.wallpaper.aurora2,1;Polaris Office,com.infraware.PolarisOfficeStdForTablet,2077500580;Recortar,com.lifevibes.trimapp,1;ChocoEUKor,com.monotype.android.font.chococooky,1;CoolEUKor,com.monotype.android.font.cooljazz,1;Helv Neue S,com.monotype.android.font.helvneuelt,1;RoseEUKor,com.monotype.android.font.rosemary,1;CatLog,com.nolanlawson.logcat,42;Fermax example,com.okode.linphone,1;Samsung account,com.osp.app.signin,140266;Peel Smart Remote,com.peel.app,30342;VideoStreaming,com.s2.videostreaming,1;SpaceIT,com.s2grupo.spaceit,1;Samsung TTS,com.samsung.SMT,0;Ajustes de licencia,com.samsung.android.app.divx,0;Deep sea,com.samsung.android.livewallpaper.deepsea,1;Luminous dots,com.samsung.android.livewallpaper.luminousdots,1;com.samsung.app.playreadyui,com.samsung.app.playreadyui,1;AvrcpServiceSamsung,com.samsung.avrcp,16;Reproduccion de grupo,com.samsung.groupcast,1005058;Ayuda,com.samsung.helphub,1;Music Hub,com.samsung.music,1;INDIServiceManager,com.samsung.scrc.idi.server,2290904;CSC,com.samsung.sec.android.application.csc,16;Idea Sketch,com.samsung.sec.sketch,2;ShareShotService,com.samsung.shareshot,1;Gmail,com.samsung.spell.gmail,1;Maps,com.samsung.spell.googlemaps,1;Busqueda de Google,com.samsung.spell.googlesearch,1;Topic Wall,com.samsung.topicwall,1;Video Hub,com.samsung.videohub,1193;Ajustes USB,com.sec.android.Kies,1;AllShare Service,com.sec.android.allshare.framework,10;DataCreate,com.sec.android.app.DataCreate,1;Wi-Fi Direct,com.sec.android.app.FileShareClient,1;Uso compartido de Wi-Fi Direct,com.sec.android.app.FileShareServer,1;SecSetupWizard,com.sec.android.app.SecSetupWizard,1;com.sec.android.app.SuggestionService,com.sec.android.app.SuggestionService,1;BluetoothTest,com.sec.android.app.bluetoothtest,1;Camara,com.sec.android.app.camera,1;Alarma,com.sec.android.app.clockpackage,1;Administrador de tareas,com.sec.android.app.controlpanel,1;Factory Mode,com.sec.android.app.factorymode,1;Game Hub,com.sec.android.app.gamehub,13010801;Kies mediante Wi-Fi,com.sec.android.app.kieswifi,2;Inicio TouchWiz,com.sec.android.app.launcher,16;Lcdtest,com.sec.android.app.lcdtest,1;com.sec.android.app.minimode.res,com.sec.android.app.minimode.res,16;Impresion movil,com.sec.android.app.mobileprint,21;Reproductor de musica,com.sec.android.app.music,1;Mis archivos,com.sec.android.app.myfiles,1;Perso,com.sec.android.app.personalization,16;PhoneUtil,com.sec.android.app.phoneutil,1;Calculadora,com.sec.android.app.popupcalculator,1;PopupuiReceiver,com.sec.android.app.popupuireceiver,1;Samsung Apps,com.sec.android.app.samsungapps,4700060;SamsungAppsUNA2,com.sec.android.app.samsungapps.una2,2035;Self Test Mode,com.sec.android.app.selftestmode,1;Service mode,com.sec.android.app.servicemodeapp,16;Nota S,com.sec.android.app.snotebook,1309093781;SNS,com.sec.android.app.sns3,10;SurfSetProp,com.sec.android.app.surfsetprop,1;SysScope,com.sec.android.app.sysscope,4;TwDVFSApp,com.sec.android.app.twdvfs,1;Editor de video,com.sec.android.app.ve,4;Reproductor de video,com.sec.android.app.videoplayer,1;SecWallpaperChooser,com.sec.android.app.wallpaperchooser,16;com.sec.android.app.wfdbroker,com.sec.android.app.wfdbroker,100;WlanTest,com.sec.android.app.wlantest,1;Reloj mundial,com.sec.android.app.worldclock,1;CloudAgent,com.sec.android.cloudagent,1;Dropbox,com.sec.android.cloudagent.dropboxoobe,1;ContextAwareService,com.sec.android.contextaware,16;Weather Daemon,com.sec.android.daemonapp.ap.accuweather,1;News Daemon(EUR),com.sec.android.daemonapp.ap.yahoonews,1;Yahoo! Finance Daemon,com.sec.android.daemonapp.ap.yahoostock.stockclock,1;DirectShareManager,com.sec.android.directshare,2;OmaDrmPopup,com.sec.android.drmpopup,1;Actualizac de software,com.sec.android.fotaclient,1;AllShare Cast Dongle S\\/W Update,com.sec.android.fwupgrade,1800011;Galeria,com.sec.android.gallery3d,30682;Comando rapido,com.sec.android.gesturepad,1;Teclado Samsung,com.sec.android.inputmethod,1;Pruebe el desplazamiento,com.sec.android.motions.settings.panningtutorial,1;Dispositivos cercanos,com.sec.android.nearby.mediaserver,16;Application installer,com.sec.android.preloadinstaller,1;BadgeProvider,com.sec.android.provider.badge,1;LogsProvider,com.sec.android.provider.logsprovider,16;Nota S,com.sec.android.provider.snote,1304012187;com.sec.android.providers.downloads,com.sec.android.providers.downloads,16;Copia de seg. y restaur.,com.sec.android.sCloudBackupApp,131;Samsung Backup Provider,com.sec.android.sCloudBackupProvider,14;Samsung Cloud Data Relay,com.sec.android.sCloudRelayData,201008;Samsung Syncadapters,com.sec.android.sCloudSync,269;Samsung Browser SyncAdapter,com.sec.android.sCloudSyncBrowser,1;Samsung Calendar SyncAdapter,com.sec.android.sCloudSyncCalendar,1;Samsung Contact SyncAdapter,com.sec.android.sCloudSyncContacts,1;Samsung SNote SyncAdapter,com.sec.android.sCloudSyncSNote,1;SASlideShow,com.sec.android.saslideshow,1;CapabilityManagerService,com.sec.android.service.cm,2;Ajustes,com.sec.android.signaturelock,1;Widget de Planificador S,com.sec.android.widgetapp.SPlannerAppWidget,1;AllShare Cast,com.sec.android.widgetapp.allsharecast,1;Reloj (moderno),com.sec.android.widgetapp.analogclocksimple,1;Yahoo! News,com.sec.android.widgetapp.ap.yahoonews,1;Weather Widget Main,com.sec.android.widgetapp.at.hero.accuweather,1;Weather Widget,com.sec.android.widgetapp.at.hero.accuweather.widget,1;Yahoo! Finance,com.sec.android.widgetapp.at.yahoostock.stockclock,1;Reloj digital,com.sec.android.widgetapp.digitalclock,1;Reloj dual (analogico),com.sec.android.widgetapp.dualclockanalog,1;Reloj dual (digital),com.sec.android.widgetapp.dualclockdigital,1;Monitor de aplicaciones,com.sec.android.widgetapp.programmonitorwidget,1;Manual de usuario,com.sec.android.widgetapp.webmanual,1;Error,com.sec.app.RilErrorNotifier,1;com.sec.bcservice,com.sec.bcservice,1;ChatON,com.sec.chaton,300450243;DSMForwarding,com.sec.dsm.phone,16;DSMLawmo,com.sec.dsm.system,1;EnterprisePermissions,com.sec.enterprise.permissions,1;Factory Test,com.sec.factory,1;MiniTaskcloserService,com.sec.minimode.taskcloser,1;Learning Hub,com.sec.msc.learninghub,13072501;AllShare Play,com.sec.pcw,3302;Remote Controls,com.sec.pcw.device,40;com.sec.phone,com.sec.phone,1;FlashAnnotate,com.sec.spen.flashannotate,1;FlashAnnotateSvc,com.sec.spen.flashannotatesvc,1;Samsung Push Service,com.sec.spp.push,91;Muro de fotos,com.siso.photoWall,1;SyncmlDS,com.smlds,1;Explorer,com.speedsoftware.explorer,34;S Suggest,com.tgrape.android.radar,3286;Ping & DNS,com.ulfdittmer.android.ping,79;Resource Manager,com.visionobjects.resourcemanager,1;S Voice,com.vlingo.midas,1000;OMACP,com.wsomacp,4;wssyncmlnps,com.wssnps,2;Actualizacion de software,com.wssyncmldm,2;OpenVPN Remote,de.blinkt.openvpn.remote,0;_MUSES,eu.musesproject.client,1;Sweden Connectivity,eu.musesproject.musesawareapp,1;MusesAwareAppTestTest,eu.musesproject.musesawareapp.test,1;OpenVPN Connect,net.openvpn.openvpn,56;Alfresco,org.alfresco.mobile.android.application,30;SmartcardService,org.simalliance.openmobileapi.service,2;VLC,org.videolan.vlc.betav7neon,9800\",\"packagename\":\"init\",\"appname\":\"init\",\"packagestatus\":\"init\",\"appversion\":\"-1\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"type\":\"virus_found\",\"timestamp\":1408434706973,\"properties\":{\"path\":\"\\/sdcard\\/Swe\\/virus.txt\",\"severity\":\"high\",\"name\":\"serious_virus\"}},\"username\":\"muses\",\"device_id\":\"9aa326e4fd9ccf61\",\"requesttype\":\"online_decision\"}";
	private final String testOpenAssetUC6 = null;
	private final String testScreenLockDisable = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1410356356486,\"appversion\":\"1\",\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.phone, com.google.process.gapps, com.lge.systemserver, com.android.smspush, com.google.android.talk, com.android.systemui, com.google.android.music:main, com.google.android.gms.wearable, com.lge.systemserver, com.lge.ime, com.lge.osp, com.google.android.gms, com.lge.lmk, system, com.lge.sync, com.android.phone, com.android.systemui, com.lge.ime, com.lge.ime, com.lge.systemserver, com.estrongs.android.pop, com.google.process.location, com.lge.music, android.process.media, com.lge.sizechangable.musicwidget.widget, com.google.process.gapps, com.google.process.location, com.google.android.music:main, com.lge.lgfotaclient:remote, eu.musesproject.client, com.lge.mlt, com.google.process.gapps, eu.musesproject.client, com.android.phone, system, com.google.process.gapps, com.google.process.location, com.google.process.location, com.google.android.tts, com.lge.smartshare.service, com.google.process.gapps, com.lge.sizechangable.weather]\",\"appname\":\"Sweden Connectivity\",\"packagename\":\"eu.musesproject.musesawareapp\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"mobileconnected\":\"false\",\"wifiencryption\":\"[WPA2-PSK-TKIP+CCMP][ESS]\",\"timestamp\":1410356610171,\"bssid\":\"24:a4:3c:04:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"8\",\"hiddenssid\":\"false\",\"networkid\":\"0\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_DEVICE_PROTECTION\":{\"id\":\"1\",\"isrooted\":\"false\",\"isrootpermissiongiven\":\"false\",\"timestamp\":1410356610171,\"ipaddress\":\"172.17.1.52\",\"ispasswordprotected\":\"false\",\"screentimeoutinseconds\":\"15\",\"istrustedantivirusinstalled\":\"true\",\"musesdatabaseexists\":\"true\",\"type\":\"CONTEXT_SENSOR_DEVICE_PROTECTION\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"2\",\"timestamp\":1410348330382,\"installedapps\":\"Sistema Android,android,16;LGSetupWizard,com.android.LGSetupWizard,43014;com.android.backupconfirm,com.android.backupconfirm,16;Compartir con Bluetooth,com.android.bluetooth,16;Internet,com.android.browser,1;Calculadora,com.android.calculator2,30000037;..._MUSES,eu.musesproject.client,1;Sweden Connectivity,eu.musesproject.musesawareapp,1;FileShare,itectokyo.fileshare.ics20,20100031;Direct Beam,itectokyo.wiflus.directbeam,10000020;LGSmartcardService,org.simalliance.openmobileapi.service,3\",\"packagename\":\"eu.musesproject.musesawareapp\",\"appname\":\"Sweden Connectivity\",\"packagestatus\":\"INSTALLED\",\"appversion\":\"1\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"timestamp\":1410356612042,\"type\":\"ACTION_SEND_MAIL\",\"properties\":{\"to\":\"the.reiceiver@generic.com, another.direct.receiver@generic.com\",\"noAttachments\":\"1\",\"subject\":\"MUSES sensor status subject\",\"path\":\"sdcard\",\"bcc\":\"hidden.reiceiver@generic.com\",\"attachmentInfo\":\"pdf\",\"from\":\"max.mustermann@generic.com\",\"cc\":\"other.listener@generic.com, 2other.listener@generic.com\"}},\"username\":\"muses\",\"device_id\":\"358648051980583\",\"requesttype\":\"online_decision\"}";
	private final String testScreenLockDisableInAction = "{\"sensor\":{\"CONTEXT_SENSOR_APP\":{\"id\":\"3\",\"timestamp\":1410356356486,\"appversion\":\"1\",\"type\":\"CONTEXT_SENSOR_APP\",\"backgroundprocess\":\"[com.android.phone, com.google.process.gapps, com.lge.systemserver, com.android.smspush, com.google.android.talk, com.android.systemui, com.google.android.music:main, com.google.android.gms.wearable, com.lge.systemserver, com.lge.ime, com.lge.osp, com.google.android.gms, com.lge.lmk, system, com.lge.sync, com.android.phone, com.android.systemui, com.lge.ime, com.lge.ime, com.lge.systemserver, com.estrongs.android.pop, com.google.process.location, com.lge.music, android.process.media, com.lge.sizechangable.musicwidget.widget, com.google.process.gapps, com.google.process.location, com.google.android.music:main, com.lge.lgfotaclient:remote, eu.musesproject.client, com.lge.mlt, com.google.process.gapps, eu.musesproject.client, com.android.phone, system, com.google.process.gapps, com.google.process.location, com.google.process.location, com.google.android.tts, com.lge.smartshare.service, com.google.process.gapps, com.lge.sizechangable.weather]\",\"appname\":\"Sweden Connectivity\",\"packagename\":\"eu.musesproject.musesawareapp\"},\"CONTEXT_SENSOR_CONNECTIVITY\":{\"id\":\"3\",\"mobileconnected\":\"false\",\"wifiencryption\":\"[WPA2-PSK-TKIP+CCMP][ESS]\",\"timestamp\":1410356610171,\"bssid\":\"24:a4:3c:04:ae:09\",\"bluetoothconnected\":\"FALSE\",\"wifienabled\":\"true\",\"wifineighbors\":\"8\",\"hiddenssid\":\"false\",\"networkid\":\"0\",\"type\":\"CONTEXT_SENSOR_CONNECTIVITY\",\"wificonnected\":\"true\",\"airplanemode\":\"false\"},\"CONTEXT_SENSOR_DEVICE_PROTECTION\":{\"id\":\"1\",\"isrooted\":\"false\",\"isrootpermissiongiven\":\"false\",\"timestamp\":1410356610171,\"ipaddress\":\"172.17.1.52\",\"ispasswordprotected\":\"false\",\"screentimeoutinseconds\":\"15\",\"istrustedantivirusinstalled\":\"true\",\"musesdatabaseexists\":\"true\",\"type\":\"CONTEXT_SENSOR_DEVICE_PROTECTION\"},\"CONTEXT_SENSOR_PACKAGE\":{\"id\":\"2\",\"timestamp\":1410348330382,\"installedapps\":\"Sistema Android,android,16;LGSetupWizard,com.android.LGSetupWizard,43014;com.android.backupconfirm,com.android.backupconfirm,16;Compartir con Bluetooth,com.android.bluetooth,16;Internet,com.android.browser,1;Calculadora,com.android.calculator2,30000037;..._MUSES,eu.musesproject.client,1;Sweden Connectivity,eu.musesproject.musesawareapp,1;FileShare,itectokyo.fileshare.ics20,20100031;Direct Beam,itectokyo.wiflus.directbeam,10000020;LGSmartcardService,org.simalliance.openmobileapi.service,3\",\"packagename\":\"eu.musesproject.musesawareapp\",\"appname\":\"Sweden Connectivity\",\"packagestatus\":\"INSTALLED\",\"appversion\":\"1\",\"type\":\"CONTEXT_SENSOR_PACKAGE\"}},\"action\":{\"timestamp\":1410356612042,\"type\":\"security_property_changed\",\"properties\":{\"property\":\"SCREEN_LOCK_TYPE\",\"value\":\"None\"}},\"username\":\"muses\",\"device_id\":\"358648051980583\",\"requesttype\":\"online_decision\"}";
	
	public final void testFullCycleWithClues(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		
		List<ContextEvent> list = JSONManager.processJSONMessage(testFullCycleWithClues, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	public final void testSecurityDeviceStateChange(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		
		List<ContextEvent> list1 = JSONManager.processJSONMessage(testSecurityDeviceStateStep1, "online_decision");
		List<ContextEvent> list2 = JSONManager.processJSONMessage(testSecurityDeviceStateStep2, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
		for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	public final void testCluesDeviceStateSecurityIncident(){
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		
		List<ContextEvent> list = JSONManager.processJSONMessage(testFullCycleWithClues, "online_decision");
		List<ContextEvent> list1 = JSONManager.processJSONMessage(testSecurityDeviceStateStep1, "online_decision");
		List<ContextEvent> list2 = JSONManager.processJSONMessage(testSecurityDeviceStateStep2, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
		for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
		for (Iterator iterator = list2.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	public final void testSecurityIncident(){
		
		User user = new User();
		UserTrustValue value = new UserTrustValue();
		value.setValue(1);
		user.setUsertrustvalue(value);
		
		SecurityIncident securityIncident = new SecurityIncident();
		securityIncident.setAssetid(1);
		securityIncident.setCostBenefit(1); //Included in the UI
		securityIncident.setDecisionid(0);
		securityIncident.setDescription("");
		securityIncident.setProbability(0.5);
		securityIncident.setUser(user);
		Probability probability = new Probability();
		probability.setValue(0.5);
		
		Rt2aeGlobal.notifySecurityIncident(probability, securityIncident);
		assertNotNull(user.getUsertrustvalue());
		
	}
	

	
	
	public final void testUserAction(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		
		List<ContextEvent> list = JSONManager.processJSONMessage(testUserAction, "user_behavior");
		
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
		
	}
	
	public final void testPolicyOpenConfAssetSecure(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testOpenConfAssetSecure, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testOpenConfAssetSecure);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
public final void testPolicyOpenConfAssetInSecure(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testOpenConfAssetInSecure, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testOpenConfAssetInSecure);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	public final void testPolicyOpenBlacklistApp(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testBlacklistApp, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testBlacklistApp);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	
	public final void testPolicyOpenNotBlacklistApp(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testNotBlacklistApp, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testNotBlacklistApp);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}

	public final void testPolicyEmailWithoutAttachments(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testEmailWithoutAttachments, "online_decision");		
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator<ContextEvent> iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testEmailWithoutAttachments);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	public final void testPolicyEmailWithAttachmentsVirusFound(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testEmailWithAttachments, "online_decision");
		//List<ContextEvent> list = JSONManager.processJSONMessage(testEmailWithAttachmentsReal1, "online_decision");
		//List<ContextEvent> list1 = JSONManager.processJSONMessage(testVirusFound, "online_decision");
		List<ContextEvent> list1 = JSONManager.processJSONMessage(testVirusFoundReal, "online_decision");
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator<ContextEvent> iterator = list1.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);

			JSONObject root;
			try {
				root = new JSONObject(testVirusFound);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			des.insertFact(formattedEvent);
		}
		
		//DeviceSecurityState changes due to virus found in the same device
		//testSecurityDeviceStateChange();
		for (Iterator<ContextEvent> iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			
			JSONObject root;
			try {
				root = new JSONObject(testEmailWithAttachments);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
		
		/*
		testSecurityIncident();// TODO Associate with the same user and previous decision
		testUserAction(); //TODO Associate with the same user*/
	}
	
	
	//Features for prototype 1
	
	public final void testPolicyScreenLockDisable(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testScreenLockDisableInAction, "online_decision");		
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator<ContextEvent> iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testScreenLockDisableInAction);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
	public final void testPolicyACLForDemoUC6(){
		
		EventProcessor processor = null;
		MusesCorrelationEngineImpl engine = null;
		List<ContextEvent> list = JSONManager.processJSONMessage(testOpenAssetUC6, "online_decision");		
		DroolsEngineService des = EventProcessorImpl.getMusesEngineService();
		if (des==null){
			processor = new EventProcessorImpl();
			engine = (MusesCorrelationEngineImpl)processor.startTemporalCorrelation("/drl");
			assertNotNull(engine);
			des = EventProcessorImpl.getMusesEngineService();
		}
		
		for (Iterator<ContextEvent> iterator = list.iterator(); iterator.hasNext();) {
			ContextEvent contextEvent = (ContextEvent) iterator.next();
			assertNotNull(contextEvent);
			Event formattedEvent = UserContextEventDataReceiver.getInstance().formatEvent(contextEvent);
			JSONObject root;
			try {
				root = new JSONObject(testOpenAssetUC6);
				formattedEvent.setSessionId(defaultSessionId);
				formattedEvent.setUsername(root
						.getString(JSONIdentifiers.AUTH_USERNAME));
				formattedEvent.setDeviceId(root
						.getString(JSONIdentifiers.AUTH_DEVICE_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			formattedEvent.setSessionId(defaultSessionId);
			des.insertFact(formattedEvent);
		}
	}
	
}
