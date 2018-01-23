package com.evanhoe.tango;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.evanhoe.tango.R;

import com.evanhoe.tango.dao.InjectionStationDAO;
import com.evanhoe.tango.objs.InjectionStation;
import com.evanhoe.tango.objs.Unit;
import com.evanhoe.tango.objs.User;
import com.evanhoe.tango.utils.BluetoothService;
import com.evanhoe.tango.utils.CommonUtilities;

public class SettingsActivity extends Activity {

    ArrayList<Unit> units = new ArrayList<Unit>();
    SettingsBroadcastReceiver myReceiver = new SettingsBroadcastReceiver();
    UnitArrayAdapter unitViewAdapter = null;

    /*
     * This method is executed when this Activity is created.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        unitViewAdapter = new UnitArrayAdapter ( this, R.layout.unit_list_row, units );
        ListView listView = (ListView) findViewById(R.id.unitList);
        listView.setAdapter(unitViewAdapter);
        listView.setOnItemClickListener ( new UnitListOnClickListener ( ) );

    }

    /*
     * This method is executed when this Activity is sent to the background.
     *
     */
    @Override
    protected void onPause() 
    {
        super.onPause();
        BluetoothService.stopSearchForDevices ( this, myReceiver );
        this.unregisterReceiver ( myReceiver );
    }

    @Override
    protected void onRestart() 
    {
        super.onRestart();
    }
    @Override
    protected void onStart() 
    {
        super.onStart();
    }
    /*
     * This method is executed when this Activity is brought back into the foreground.
     *
     */
    @Override
    protected void onResume() 
    {
        super.onResume();
        units.clear();
        unitViewAdapter.notifyDataSetChanged();
        addUnitsToList ( BluetoothService.getPairedDevices() );

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction ( BluetoothDevice.ACTION_FOUND );
        intentFilter.addAction ( BluetoothAdapter.ACTION_DISCOVERY_STARTED );
        intentFilter.addAction ( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
        intentFilter.addAction ( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
        intentFilter.addAction ( BluetoothDevice.ACTION_ACL_CONNECTED );
        intentFilter.addAction ( BluetoothDevice.ACTION_ACL_DISCONNECTED );
        intentFilter.addAction ( BluetoothDevice.ACTION_BOND_STATE_CHANGED );
        registerReceiver ( myReceiver, intentFilter );

        BluetoothService.searchForDevices ( this, myReceiver );
    }

    /*
     * This method will process a list of Unit objects through the
     * addUnitToList method.
     *
     * @param The List of Unit objects to add to the list.
     */
    private void addUnitsToList ( ArrayList<Unit> unitList )
    {
        // Loop through list passed in, and call the main addUnit function
        for ( Unit thisUnit : unitList )
        {
            addUnitToList ( thisUnit );
        }

        return;
    }

    /*
     * This method will add a Unit object to the list if it doesn't already exist.
     *
     * @param The Unit object to add to the list.
     */
    private void addUnitToList ( Unit addUnit )
    {
        boolean doesExist = false;

        // Check to see if the adding Unit already exists
        for ( Unit currentUnit : units )
        {
            if ( currentUnit.getMacAddress().equals ( addUnit.getMacAddress() ) )
            {
                doesExist = true;
            }
        }

        // Is this unit authorized for this technician?
        boolean isAuthorized = false;
        User thisUser = ((TangoApplication) this.getApplication()).getUser();
        Context appContext = ((TangoApplication) this.getApplication()).getApplicationContext();
     System.out.print ( ">>>>> Adding Unit [" + addUnit.getMacAddress() + "]..." );
        isAuthorized = CommonUtilities.isUnitAuthorized ( appContext, addUnit.getMacAddress(), thisUser.getPersonID() );
     System.out.print ( isAuthorized );

      // If the adding Unit doesn't exist, then add it to the list.
        if ( ! doesExist && isAuthorized )
        {
            String unitID = InjectionStationDAO.getID ( ((TangoApplication) this.getApplication()).getApplicationContext(), addUnit.getMacAddress() );
            if ( unitID != null )
            {
                InjectionStation thisStation = InjectionStationDAO.get ( ((TangoApplication) this.getApplication()).getApplicationContext(), unitID );
                addUnit.setBtName(addUnit.getUnitName());
                addUnit.setUnitName ( thisStation.getInjectionStationName() );
            }
            units.add ( addUnit );
            unitViewAdapter.notifyDataSetChanged();
        }
     //  addUnit.setBtName(addUnit.getUnitName());
      // addUnit.setUnitName(addUnit.getUnitName());
        //units.add ( addUnit );
        //unitViewAdapter.notifyDataSetChanged();
    }

    // sehttp://www.javacodegeeks.com/2013/09/android-listview-with-adapter-example.html
    class UnitArrayAdapter extends ArrayAdapter<Unit> 
    {
        private Context context;
        private int layoutResourceId;
        private ArrayList<Unit> units = null;

        public UnitArrayAdapter(Context context, int layoutResourceId, ArrayList<Unit> units) {

            super(context, layoutResourceId, units);

            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.units = units;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {

            if (convertView == null) 
            {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            Unit unit = units.get(position);

            // Set the text that will be seen - the name of the unit
            ((TextView) convertView.findViewById(R.id.unitName)).setText(unit.getUnitName());

            // If we are paired via bluetooth, state that we are "Connected"
            ((TextView) convertView.findViewById(R.id.unitWorking)).setText ( unit.getPairedString() );
            //if ( unit.getPaired() == Unit.PAIRED)
            //{
            //}
            //else
            //{
            //    convertView.findViewById(R.id.unitWorking).setVisibility(View.INVISIBLE);
            //}

            // Show the check mark if this unit is the current selection
            String usersChosenMacAddress = ((TangoApplication) getApplicationContext()).getUnitMacAddress();
            if ( usersChosenMacAddress != null && usersChosenMacAddress.equals ( unit.getMacAddress() ) )
            {
                convertView.findViewById(R.id.unitsWorkingIcon).setVisibility(View.VISIBLE);
            }
            else
            {
                convertView.findViewById(R.id.unitsWorkingIcon).setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    /*
     * This class is used to handle clicks in the ListView showing the devices in Bluetooth range.
     */
    class UnitListOnClickListener implements OnItemClickListener
    {
        @Override
        public void onItemClick ( AdapterView<?> parent, View view, int position, long id )
        {
            // Get MAC Addresses from the application for current, and ListView for selection
            String currentMacAddress = ((TangoApplication) getApplicationContext()).getUnitMacAddress();
            String selectedMacAddress = units.get(position).getMacAddress();
            int selectedPairStatus = units.get(position).getPaired();
            boolean success = false;
            boolean currentDevicePaired = false;

            // 1. If there is no current application selected unit 
            // 2. If there is no selection from the list view (should never happen)
            // 3. If the current unit doesn't match clicked on unit
            // 4. If the current unit matches clicked on unit, but unit isn't paired
            if ( currentMacAddress == null ||
                 selectedMacAddress == null ||
                 ! currentMacAddress.equals ( selectedMacAddress ) ||
                 ( currentMacAddress.equals ( selectedMacAddress ) && selectedPairStatus != Unit.UNIT_PAIRED ) )
            {
                // Only attempt unpairing if we have a currentMacAddress stored
                if ( currentMacAddress != null )
                {
                    // Loop through all items in the listview
                    for ( Unit currentUnit : units )
                    {
                        // If we find the current application MAC address, unpair
                        if ( currentUnit.getMacAddress().equals ( currentMacAddress ) &&
                             currentUnit.getPaired() == Unit.UNIT_PAIRED )
                        {
System.out.println ( "Current unit ["+currentUnit.getMacAddress()+"] is paired..." );
                            currentDevicePaired = true;
                            try
                            {
                                success = BluetoothService.unpairDevice ( currentUnit.getBluetoothDevice() );
                                if ( success )
                                {
System.out.println ( "Unpairing was successful." );
                                    currentUnit.setPaired ( Unit.UNIT_NOT_PAIRED );

                                    // Since we have unpaired, clear out current address
                                    currentMacAddress = "";
                                    ((TangoApplication) getApplicationContext()).setUnitMacAddress(null);
                                    ((UnitArrayAdapter) parent.getAdapter()).notifyDataSetChanged();

                                }
else
{
System.out.println ( "Unpairing was unsuccessful." );
}
                            }
                            catch ( Exception e )
                            {
                                success = false;
                            }
                        }
                    }
                }

                // If no device currently paired, or if the unpair was successful
                if ( !currentDevicePaired || success )
                {
                    try
                    {
System.out.println ( ">>>>>>>>>> Pairing to device [ " + units.get(position).getBluetoothDevice() + " ]..." );
                        // Pair to the new selected device
                        success = BluetoothService.pairDevice ( units.get(position).getBluetoothDevice() );
                    }
                    catch ( Exception e )
                    {
                        success = false;
                    }

                    // If the pair was successful, set the application MAC address, and reset the ListView
                    // Irrespective of the pair being successful or not, if the unit is already paired
                    // then just set this Mac address and move on.
                    Unit selectedUnit = units.get(position);
                    if ( success || selectedUnit.getPaired() == Unit.UNIT_PAIRED )
                    {
//TODO : Do not set paired-status here. This is just a request to pair, doesn't prove anything.
// This should be done in the BroadcastReceiver class.
//                            units.get(position).setPaired ( Unit.UNIT_PAIR_IN_PROGRESS );
//                            ((UnitArrayAdapter) parent.getAdapter()).notifyDataSetChanged();
                        ((TangoApplication) getApplicationContext()).setUnitMacAddress ( selectedMacAddress );
                        ((TangoApplication) getApplicationContext()).setUnitBtName(selectedUnit.getBtName());
                        ((UnitArrayAdapter) parent.getAdapter()).notifyDataSetChanged();
                    }
else
{
System.out.println ( "Pairing was unsuccessful." );
}
                }
            }
        }
    }

    /*
     * This class is used to listen to the Broadcast and handle specific Bluetooth events.
     */
    class SettingsBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive ( Context context, Intent intent )
        {
            String action = intent.getAction();
            ProgressBar pbSettings = (ProgressBar) findViewById ( R.id.pbSettings );
System.out.println ( ">>>> ACTION: " + action );
            if ( action.equals ( BluetoothAdapter.ACTION_DISCOVERY_STARTED ) )
            {
                pbSettings.setVisibility ( View.VISIBLE );
            }
            else if ( action.equals ( BluetoothAdapter.ACTION_DISCOVERY_FINISHED ) )
            {
                pbSettings.setVisibility ( View.GONE );
            }
            else if ( action.equals ( BluetoothDevice.ACTION_FOUND ) )
            {
                BluetoothDevice newDevice = intent.getParcelableExtra ( BluetoothDevice.EXTRA_DEVICE );
                Unit newUnit = new Unit ( newDevice.getAddress(), newDevice.getName(), Unit.UNIT_NOT_PAIRED );
                newUnit.setBluetoothDevice ( newDevice );
                addUnitToList ( newUnit );
            }
            else if ( action.equals ( BluetoothDevice.ACTION_ACL_CONNECTED ) )
            {
                // update list to show connected
                // TODO: update the list somehow to show that we are now paired.
                //   Probably a function updateUnitPairedStatus ( macAddress, true );
            }
            else if ( action.equals ( BluetoothDevice.ACTION_ACL_DISCONNECTED ) )
            {
                // update list to show disconnected
                // devices send this after the handshake. You don't "stay" connected.
            }
            else if ( action.equals ( BluetoothDevice.ACTION_BOND_STATE_CHANGED ) )
            {
                // We have a change in bonding status...
                // Get the device in question
                BluetoothDevice newDevice = intent.getParcelableExtra ( BluetoothDevice.EXTRA_DEVICE );

                Unit bondingUnit = null;
                // Get the item from the list
                for ( Unit currentUnit : units )
                {
                    if ( currentUnit.getMacAddress().equals ( newDevice.getAddress() ) )
                    {
                        bondingUnit = currentUnit;
                    }
                }

                // Only worry about change in bonding status, if it is in our list
                if ( bondingUnit != null )
                {
                    // What is the old status
                    //int oldState = intent.getIntExtra ( BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR );

                    // What is the new status
                    int newState = intent.getIntExtra ( BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR );

                    // What do we do in each case
                    // Old = BOND_NONE New = BOND_BONDING
                    if ( /*oldState == BluetoothDevice.BOND_NONE &&*/
                         newState == BluetoothDevice.BOND_BONDING )
                    {
                         bondingUnit.setPaired ( Unit.UNIT_PAIR_IN_PROGRESS );
                    }
                    // Old = BOND_BONDING New = BOND_BONDED
                    else if ( /*oldState == BluetoothDevice.BOND_BONDING &&*/
                              newState == BluetoothDevice.BOND_BONDED )
                    {
                         bondingUnit.setPaired ( Unit.UNIT_PAIRED );
                    }
                    // Old = BOND_BONDED New = BOND_NONE
                    else if ( /*oldState == BluetoothDevice.BOND_BONDED &&*/
                              newState == BluetoothDevice.BOND_NONE || 
                              newState == BluetoothDevice.ERROR )
                    {
                         bondingUnit.setPaired ( Unit.UNIT_NOT_PAIRED );
                    }

                    // Let's update the ListView
                    unitViewAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
