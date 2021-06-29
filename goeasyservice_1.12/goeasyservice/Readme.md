# GoeasyService

Service that allows the retrieval of data from a ublox device through usb and converts it to its corresponding Gnss fields.


## GoeasyServiceExample
Sample android project with a basic app that consumes the GoeasyService library and presents it in the screen.

Once installed, when the receiver is connected though usb, a GoeasyService will be started in the background.  
The app allows connecting to that service and presenting the data.


## Goeasygnssdatalib

Full library containing the different classes and methods to retrieve the data.

You can include the full code after importing as a module in your app build.gradle:

    implementation project(path: ':goeasygnssdatalib')

Or you can use the prebuilt aar library, including it in app/libs/ and importing as such:

    implementation(name: 'goeasygnssdatalib-release', ext:'aar')

Also, you may have to include "dirs" in your project build gradle repositories

    allprojects {
        repositories {
            ...    
            flatDir {
                dirs 'libs'
            }
            ...        
        }
    }

## Using the library

There are three main components of this library,
 - a UsbEventReceiver that will start the GoeasyService after connecting the usb receiver
 - the GoeasyService that will retrieve the data from the device and parse it periodically
 - a GnssDataListener that will receive the data from the service to the application

### UsbEventReceiver

The UsbEventReceiverActivity should be declared in your manifest, as it will be launched when a usb device has been connected

    <activity
        android:name="com.bq.goeasygnssdatalib.UsbEventReceiverActivity"
        android:excludeFromRecents="true"
        android:exported="false"
        android:noHistory="true"
        android:theme="@style/Theme.Transparent">
        <intent-filter>
            <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
        </intent-filter>
        <meta-data
            android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED"
            android:resource="@xml/usb_device_filter" />
    </activity>

If there is no need for the service to start when the device is connected, GoeasyService can be started as such:

    Intent serviceIntent = new Intent(context, GoeasyService.class);


### ServiceConnection

A ServiceConnection has to be set up to connect to the running GoeasyService, and then the service can register the activity as its listener, receiving the data changes

    `public class MainActivity extends AppCompatActivity implements GnssDataListener {
    
        private GoeasyService mBoundService;
    
        private boolean mIsBound;
    
        private ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mBoundService = ((GoeasyService.LocalBinder) service).getService();
                mBoundService.registerListener((MainActivity) myActivity);
            }
    
            public void onServiceDisconnected(ComponentName className) {
                mBoundService = null;
            }
        };
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            ...
            doBindService();
            ...    
        }
    
        void doBindService() {
            bindService(new Intent(MainActivity.this,
                    GoeasyService.class), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
    
        void doUnbindService() {
            if (mIsBound) {
                if (mBoundService != null) {
                    mBoundService.unregisterListener(this);
                }
                unbindService(mConnection);
                mIsBound = false;
            }
        }
    
        @Override
        protected void onDestroy() {
            super.onDestroy();
            doUnbindService();
        }
    
        @Override
        public void onDataChangedCallback(GnssData data) {
            // Treat your data as you wish
        }
    }`

### Data Received

    class GnssData {
        int authenticity;
        long lat;
        long lon;
        long time;
        class GalileoAuth {
            int type;
            long time;
            long fullbiasnano;
            long timenano;
            int svid;
            int status;
            int msgid;
            int submsgid;
            ArrayList<Integer> data;        
        }    
    }