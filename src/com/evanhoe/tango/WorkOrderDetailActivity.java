package com.evanhoe.tango;

import com.evanhoe.tango.dao.InjectionStationDAO;
import com.evanhoe.tango.dao.WorkorderDetailDAO;
import com.evanhoe.tango.objs.TermidorData;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.WorkOrderDetail;
import com.evanhoe.tango.objs.WorkorderStatus;
import com.evanhoe.tango.termidormsg.CanMessageServiceBCB;
import com.evanhoe.tango.termidormsg.CanMessageServiceStandard;
import com.evanhoe.tango.termidormsg.TermidorMessageInterface;
import com.evanhoe.tango.termidormsg.CANMessageService;
import com.evanhoe.tango.termidormsg.TestTermidorMessageImpl;
import com.evanhoe.tango.utils.CommonUtilities;
import com.evanhoe.tango.utils.GetLocation;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.evanhoe.tango.R;
import com.evanhoe.tango.utils.WebService;

public class WorkOrderDetailActivity extends OptionsMenuActivity implements OnClickListener {

	ListView lvwWorkOrders;
	ArrayList<String> workOrders = new ArrayList<String>();
	WorkOrder selectedWorkOrder;
	String error = "";
	String Status = "";
	TextView tv;
	TermidorData newData = new TermidorData();
	Location whereAmI;
	Button btnSendData,btnComplete,btnOnHold;
	TextView woStatus;
	ImageView statusIcon;
	boolean usUnits = true;
	String token="";
	int chker=0;
	//ProgressDialog sendReceivPB = null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences1 = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences1.getString("urltype","");
		if(selecturl.contains("stage")){
			setTheme(R.style.AppTheme);
			chker=1;
		}
		else{
			setTheme(R.style.AppTheme1);
		}
		setContentView(R.layout.activity_work_order_details);
		Button staging=(Button)findViewById(R.id.stag1);
		if(chker==0){
			staging.setVisibility(View.INVISIBLE);
		}
		SharedPreferences sharedPreferences = getSharedPreferences(
				"userdata", Context.MODE_PRIVATE);
		token=sharedPreferences.getString("token","");
		//tv = (TextView) findViewById(R.id.textViewWithScroll);
		String locale = getResources().getConfiguration().locale.getCountry();
		if (locale.equals("US"))
		{
			usUnits = true;
		}
		else
		{
			usUnits = false;
		}
		//Button btnSendData;
		btnSendData = (Button) findViewById ( R.id.btnSendData );
		if ( btnSendData != null )
		{
			btnSendData.setOnClickListener ( this );
		}
		else System.out.println ( "Send Button not found" );

		//Button btnOnHold;
		btnOnHold = (Button) findViewById ( R.id.btnOnHold );
		if ( btnOnHold != null )
		{
			btnOnHold.setOnClickListener ( this );
		}
		else System.out.println ( "Hold Button not found" );

		//Button btnComplete;
		btnComplete = (Button) findViewById ( R.id.btnComplete );
		if ( btnComplete != null )
		{
			btnComplete.setOnClickListener ( this );
		}
		else System.out.println ( "Complete Button not found" );

		woStatus = (TextView) findViewById ( R.id.woStatus );
		statusIcon = (ImageView) findViewById (R.id.statusIcon);

		Intent myIntent = getIntent();
		selectedWorkOrder = null;
		if ( myIntent != null )
		{
			selectedWorkOrder = myIntent.getParcelableExtra ( "workOrder" );
		}

		// If the work order is passed in, process it
		if ( selectedWorkOrder != null )
		{
			Double distance = selectedWorkOrder.getLinearDistancePlanned();


			String title = String.format(getString(R.string.work_order_title), selectedWorkOrder.getServiceManagementWorkorderId().toString());
			getActionBar().setTitle(title);

			TextView tvLinearDistance = (TextView) findViewById ( R.id.tvLinearDistance );
			DecimalFormat df = new DecimalFormat("0.0");
			if(!usUnits)
			{
				distance = distance * 0.3048;
				tvLinearDistance.setText ( df.format( distance )  + " " + getApplicationContext().getString(R.string.unit_distance_meters));
			}else{
				tvLinearDistance.setText ( df.format( distance )  + " " + getApplicationContext().getString(R.string.unit_distance));
			}

			TextView tvAddressLine1 = (TextView) findViewById ( R.id.tvAddress1 );
			TextView tvAddressLine2 = (TextView) findViewById ( R.id.tvAddress2 );
			TextView tvAddressCityState = (TextView) findViewById ( R.id.tvAddressCityState );
			//TextView tvLinearDistance = (TextView) findViewById ( R.id.tvLinearDistance );
			//tvLinearDistance.setText ( df.format( distance )  + " " + getApplicationContext().getString(R.string.unit_distance));
			if (getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels)
			{
				tvAddressCityState.setVisibility(View.GONE);
				tvAddressLine1.setText (selectedWorkOrder.getAddressLine1() + " " + selectedWorkOrder.getCity()+", "+selectedWorkOrder.getStateProvinceCode());
			}
			else
			{
				tvAddressCityState.setVisibility(View.VISIBLE);
				tvAddressLine1.setText ( selectedWorkOrder.getAddressLine1() );
				tvAddressCityState.setText ( selectedWorkOrder.getCity()+", "+selectedWorkOrder.getStateProvinceCode() );

			}
			tvAddressLine2.setText ( selectedWorkOrder.getAddressLine2() );
			if(selectedWorkOrder.getAddressLine2().trim().equalsIgnoreCase("")){
				tvAddressLine2.setVisibility(View.GONE);
			}
			if(((TangoApplication) this.getApplication()).getIsTrainingMode()){
				tvAddressLine1.setText("-- Training --\n" + tvAddressLine1.getText());
			}
			// get work order details from web service
			new RetrieveDetailTask().execute();
		}
		else
		{
			// work order isn't passed, display confirmation message, then exit back to list view
			// TODO: Confirmation dialog for returning to ListView.
			finish();
		}

		// Button sendButton = (Button) findViewById ( R.id.btnSendData );
		if(CommonUtilities.areAnyWorkordersOpen(getApplicationContext())){
			btnSendData.setVisibility ( View.GONE );
			if(CommonUtilities.isThisWorkorderOpen(getApplicationContext(),selectedWorkOrder.getServiceWorkOrderId())){
				btnOnHold.setVisibility(View.VISIBLE);
				btnComplete.setVisibility(View.VISIBLE);
			}else{
				btnOnHold.setVisibility(View.GONE);
				btnComplete.setVisibility(View.GONE);
			}
		}else{
			btnSendData.setVisibility ( View.VISIBLE );
			btnOnHold.setVisibility(View.GONE);
			btnComplete.setVisibility(View.GONE);
		}
	}

	public void hideDetailData() {
		LinearLayout llDetailData = (LinearLayout) findViewById ( R.id.llDetailData);
		llDetailData.setVisibility(View.GONE);
	}

	public void showDetailData() {
		LinearLayout llDetailData = (LinearLayout) findViewById ( R.id.llDetailData);
		llDetailData.setVisibility(View.VISIBLE);
	}

	public void updateDetailDataOnScreen() {
		try {
			WorkOrderDetail thisWorkOrderDetail = WorkorderDetailDAO.getLatestByWorkOrderID(getApplicationContext(), selectedWorkOrder.getServiceWorkOrderId());
			TextView tvInjectionCount = (TextView) findViewById ( R.id.tvInjectionCount );
			TextView tvAISA = (TextView) findViewById ( R.id.tvAISA );
			TextView tvAIHT = (TextView) findViewById ( R.id.tvAIHT );
			TextView tvWaterSA = (TextView) findViewById ( R.id.tvWaterSA );
			TextView tvHTMode = (TextView) findViewById ( R.id.tvHTMode );
			TextView tvSAMode = (TextView) findViewById ( R.id.tvSAMode );
			TextView tvChemical = (TextView) findViewById ( R.id.tvChemical);
			Double saTermicideVolume = Double.parseDouble(thisWorkOrderDetail.getSA_TermicideVolume_act());
			Double htTermicideVolume = Double.parseDouble(thisWorkOrderDetail.getHT_TermicideVolume_act());
			Double saWaterVolume = Double.parseDouble(thisWorkOrderDetail.getSA_WaterVolume_act());
			String chemicalUsed = CommonUtilities.getTermicideTypeByCode(getApplicationContext(), thisWorkOrderDetail.getTermicideTypeCode_act()).getTermicideTypeName();
			DecimalFormat df = new DecimalFormat("0.00");

			if (!usUnits)
			{
				df = new DecimalFormat("0.0000");
				saTermicideVolume = saTermicideVolume * 0.0295735;
				htTermicideVolume = htTermicideVolume * 0.0295735;
				saWaterVolume = saWaterVolume * 3.78541;
				tvAISA.setText(df.format(saTermicideVolume) + " " + getApplicationContext().getString(R.string.unit_volume_liters));
				tvAIHT.setText(df.format(htTermicideVolume) + " " + getApplicationContext().getString(R.string.unit_volume_liters));
				tvWaterSA.setText(df.format(saWaterVolume) + " " + getApplicationContext().getString(R.string.unit_volume_liters));
			} else {
				tvAISA.setText(df.format(saTermicideVolume) + " " + getApplicationContext().getString(R.string.unit_volume_oz));
				tvAIHT.setText(df.format(htTermicideVolume) + " " + getApplicationContext().getString(R.string.unit_volume_oz));
				tvWaterSA.setText(df.format(saWaterVolume) + " " + getApplicationContext().getString(R.string.unit_volume_gal));
			}

			tvInjectionCount.setText(thisWorkOrderDetail.getHT_InjectionCount_act());

			tvChemical.setText( chemicalUsed );

			//   String str=thisWorkOrderDetail.getHTModeEnabled();

			if(thisWorkOrderDetail.getHTModeEnabled().equals("0")||thisWorkOrderDetail.getHTModeEnabled().equals("false"))
			{
				tvHTMode.setText("No");
			}
			else
			{
				tvHTMode.setText("Yes");
			}
			if(thisWorkOrderDetail.getSAModeEnabled().equals("0")||thisWorkOrderDetail.getSAModeEnabled().equals("false"))
			{
				tvSAMode.setText("No");
			}
			else
			{
				tvSAMode.setText("Yes");
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String showWorkorderStatus(){

		String woStatusString = CommonUtilities.getWorkOrderStatus(WorkOrderDetailActivity.this.getApplicationContext(),selectedWorkOrder.getServiceWorkOrderId());
		if ((woStatusString.trim().equals("")))
		{
			woStatusString="NEW";
		}
		if ((woStatusString.trim().equals("OPEN")))
		{
			woStatusString="IN PROGRESS";
		}
		woStatus.setText ( woStatusString );
		if(woStatusString.equalsIgnoreCase("COMPLETED")){
			btnSendData.setVisibility ( View.GONE );
			btnOnHold.setVisibility(View.GONE);
			btnComplete.setVisibility(View.GONE);
			return woStatusString;
		}
		if (statusIcon != null)
		{
			if (woStatusString.equals("IN PROGRESS"))
			{
				statusIcon.setImageResource(R.drawable.ic_dot_inprogress);
			}
			else if (woStatusString.equals("ON HOLD"))
			{
				statusIcon.setImageResource(R.drawable.ic_dot_onhold);
			}
			else
			{
//        			statusIcon.setImageResource(R.drawable.circle_bullet_blank);
			}
		}


		if(CommonUtilities.areAnyWorkordersOpen(getApplicationContext())){
			btnSendData.setVisibility ( View.GONE );
			if(CommonUtilities.isThisWorkorderOpen(getApplicationContext(),selectedWorkOrder.getServiceWorkOrderId())){
				btnOnHold.setVisibility(View.VISIBLE);
				btnComplete.setVisibility(View.VISIBLE);
			}else{
				btnOnHold.setVisibility(View.GONE);
				btnComplete.setVisibility(View.GONE);
				Toast toast = CreateToast(getResources().getColor(R.color.message_success), "Another Workorder is Open", Gravity.TOP);
				toast.show();
			}
		}else{
			btnSendData.setVisibility ( View.VISIBLE );
			btnOnHold.setVisibility(View.GONE);
			btnComplete.setVisibility(View.GONE);
		}
		return woStatusString;


	}


	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.removeItem(R.id.actionWorkOrderList);
		menu.removeItem(R.id.actionTraining);
		return true;
	}

	public void onClick(View v)
	{
		boolean onHold = false;

		// if getId is one of the 3 buttons, then validate the selected mac address from tangoapplication
		// if the mac address is null, show a message box, and then start the settings activity
		if ( v.getId() == R.id.btnSendData ||
				v.getId() == R.id.btnOnHold ||
				v.getId() == R.id.btnComplete )
		{
			String macAddress = (((TangoApplication) getApplication()).getUnitMacAddress() );
			if ( macAddress == null )
			{
				new AlertDialog.Builder(WorkOrderDetailActivity.this)
						.setTitle(getApplicationContext().getString(R.string.noUnitSelectedTitle))
						.setMessage(getApplicationContext().getString(R.string.noUnitSelectedMessage))
						.setPositiveButton(getApplicationContext().getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										Intent settingsIntent = new Intent ( WorkOrderDetailActivity.this, SettingsActivity.class );
										startActivity ( settingsIntent );
										return;
									}
								})
						.show();


				// instead of an infinite loop, make them click a button again.
				return;
			}
		}

		switch ( v.getId() )
		{
			case R.id.btnSendData:
				new AlertDialog.Builder(WorkOrderDetailActivity.this)
						.setTitle("Send Work Order")
						.setMessage(getString(R.string.dou_you_want_to_send_order,selectedWorkOrder.getServiceManagementWorkorderId()))
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										GetLocation myGPSClass = new GetLocation ( TangoApplication.getTangoApplicationContext() );
										whereAmI = myGPSClass.getLocation();
										AsyncTaskRunner runner = new AsyncTaskRunner();
										runner.execute();
										//// show hold/complete buttons, hide send button
										//manageButtons ( "OPEN" );
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										return;
									}
								})
						.show();
				break;
			case R.id.btnOnHold:
				onHold = true;
				// Allow this to fall through, as the work is the same except
				// except for button changing versus finishing the Activity
				new AlertDialog.Builder(WorkOrderDetailActivity.this)
						.setTitle("Status Change")
						.setMessage(getString(R.string.dou_you_want_onhold_order,selectedWorkOrder.getServiceManagementWorkorderId()))
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										GetLocation myGPSClass = new GetLocation ( TangoApplication.getTangoApplicationContext() );
										whereAmI = myGPSClass.getLocation();
										AsyncTaskRunner2 runner = new AsyncTaskRunner2( true, whereAmI );
										runner.execute();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										return;
									}
								})
						.show();
				break;
			case R.id.btnComplete:
				// get data


				new AlertDialog.Builder(WorkOrderDetailActivity.this)
						.setTitle("Status Change")
						.setMessage(getString(R.string.dou_you_want_complete_order,selectedWorkOrder.getServiceManagementWorkorderId()))
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										GetLocation myGPSClass = new GetLocation ( TangoApplication.getTangoApplicationContext() );
										whereAmI = myGPSClass.getLocation();
										AsyncTaskRunner2 runner = new AsyncTaskRunner2( false, whereAmI );
										runner.execute();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which)
									{
										return;
									}
								})
						.show();


				break;
			default:
				Toast.makeText(getApplicationContext(), "Cannot identify onClick view", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	/*
     * This function manages the showing and hiding of the buttons
     */
	private void manageButtons ( String status )
	{
		int sendVisible, holdCompleteVisible;

		// default to not seeing any buttons
		sendVisible = holdCompleteVisible = View.GONE;

		// if on hold, send button visible only
		if ( status.equals ( "ONHOLD" ) )
			sendVisible = View.VISIBLE;
			// if open/working, on hold and complete button visible
		else if ( status.equals ( "OPEN" ) )
			holdCompleteVisible = View.VISIBLE;
		Button sendButton, holdButton, completeButton;
		sendButton = (Button) findViewById ( R.id.btnSendData );
		holdButton = (Button) findViewById ( R.id.btnOnHold );
		completeButton = (Button) findViewById ( R.id.btnComplete );

		sendButton.setVisibility ( sendVisible );
		holdButton.setVisibility ( holdCompleteVisible );
		completeButton.setVisibility ( holdCompleteVisible );

		return;
	}



	private void writeBlankRecord(String woStatus){
		TermidorData thisData = new TermidorData();
		thisData.setHtModeInjectorCount(0);
		thisData.setWorkOrderNumber(selectedWorkOrder.getServiceManagementWorkorderId());
		thisData.setWorkOrderID(selectedWorkOrder.getServiceWorkOrderId());
		thisData.setLocation ( "xxx" );
		thisData.setPidDisplayFirmware ( "0.1.1" );
		thisData.setBaseControllerFirmware ( "0.1.1" );
		thisData.setBaseDisplayFirm ( "0.1.1" );
		thisData.setUnitID ( "01:23:45:67:89:AB" );
		thisData.setAddress ( selectedWorkOrder.getAddressLine1() );
		thisData.setCity ( selectedWorkOrder.getCity() );
		thisData.setState ( selectedWorkOrder.getStateProvinceCode() );
		thisData.setWorkOrderStartDateStamp ( "01-jan-1900 01:00:00" );
		thisData.setProductApplied ( "" );
		thisData.setSoilType ( (short) 1 );
		thisData.setInjectorTimer ( (float) 0 );
		thisData.setHtModePumpVolume ( (float) 0 );
		thisData.setHtModeInjectorCount ( 0 );
		thisData.setHtModeActiveIngredientUsage ( (float) 0 );
		thisData.setHtModeWaterUsage ( (float) 0 );
		thisData.setSaModeActiveIngredientUsage ( (float) 0 );
		thisData.setSaModeWaterUsage ( (float) 0 );
		thisData.setWorkOrderStartTime ( "01-jan-1900 01:01:01" );
		thisData.setTimeToComplete ( 0 );
		thisData.setLocationDone ( true );
		thisData.setHtModeEnabled ( false );
		thisData.setSaModeEnabled ( false );
		thisData.setPumpAlarm ( false );
		thisData.setWaterAlarm ( false );
		thisData.setHtFlowInSAMode ( false );
		WorkOrderDetail thisDetail;
		thisDetail = new WorkOrderDetail ( thisData );
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
		thisDetail.setEntryTime ( sdf.format ( cal.getTime() ) + " " + CommonUtilities.getOffsetGMT()  );
		thisDetail.setSyncTime ( "1900-01-01 12:57:00" );
		thisDetail.setSyncStatus ( "N" );
		thisDetail.setTechnicianGroupID ( ((TangoApplication) this.getApplication()).getUser().getGroupID() );
		thisDetail.setTechnicianPersonID ( ((TangoApplication) this.getApplication()).getUser().getPersonID() );
		thisDetail.setTechnicianUserRoleCode ( ((TangoApplication) this.getApplication()).getUser().getUserRoleCode() );
		thisDetail.setTechnicianUserSubRoleCode ( ((TangoApplication) this.getApplication()).getUser().getUserSubRoleCode() );
		String macAddress = ((TangoApplication) getApplication()).getUnitMacAddress();
		String injectionID = null;
		if ( macAddress != null )
		{
			injectionID = InjectionStationDAO.getID ( getApplicationContext(), macAddress );
		}
		thisDetail.setInjectionStationID_act ( injectionID );
		thisDetail.setTermicideTypeCode_act ( selectedWorkOrder.getTermicideTypeCodePlanned() );
		thisDetail.setSoilTypeCode_act((selectedWorkOrder.getSoilTypeCodePlanned()));
		thisDetail.setTermicideTypeName_act ( "Unknown" );
		thisDetail.setHT_LinearDistanceApplication_act ( "0" );
		thisDetail.setHT_InjectorTimer_act ( "0" );
		thisDetail.setSA_PumpVolume_act ( "0" );
		thisDetail.setLinearMeasurementUnit_act  ( selectedWorkOrder.getLinearMeasurementUnit());
		thisDetail.setVolumeMeasurementUnit_act  ( selectedWorkOrder.getVolumeMeasurementUnit() );
		// Null check for GPS location
		double latitude, longitude;
		latitude = longitude = 0.0;
		if ( whereAmI != null )
		{
			latitude = whereAmI.getLatitude();
			longitude = whereAmI.getLongitude();
		}
		thisDetail.setLatitude ( Double.toString ( latitude ) );
		thisDetail.setLongitude ( Double.toString ( longitude ) );
		thisDetail.setMinutesWorkedInSession_act ( "0" );
		thisDetail.setWorkorderSlotLocationUsed ( "1" );
		thisDetail.setWorkorderStatusCode ( woStatus );
		WorkorderDetailDAO.add ( WorkOrderDetailActivity.this.getApplicationContext(), thisDetail );
	}


	/*
	 * Show the returned message
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				//Use Data to get string
				String string = data.getStringExtra("RESULT_STRING");
				//tv.setText("Status: " + string);
			}
		}
	}

	/*@Override
	public void onPause() {
	    super.onPause();

	    if ((sendReceivPB != null) && sendReceivPB.isShowing()){
	    	sendReceivPB.dismiss();
	    }
	    sendReceivPB = null;
	}*/

	private class AsyncTaskRunner extends AsyncTask<String, Void, String>
	{
		boolean error = false;
		String errorString="SUCCESS";
		ProgressDialog sendReceivPB = null;
		//final TermidorMessageInterface thisService = new CANMessageService();
		final TermidorMessageInterface thisService = new CanMessageServiceStandard();
		//final TermidorMessageInterface thisService = new TestTermidorMessageImpl();



		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				boolean init = thisService.initialize(((TangoApplication) getApplicationContext()).getUnitMacAddress());
				if(init == false){
					throw new Exception("Unable to connect to Unit");
				}

				//attempt to pull data from device before sending just in case
				WorkOrderDetail thisDetail;
				//int sumInjections = 0;
				newData = thisService.getData();
				// update local db
				if ( newData != null )
				{
					if (newData.getLastErrorMessage().equals("SUCCESS"))
					{
						thisDetail = new WorkOrderDetail ( newData );

						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

						thisDetail.setEntryTime ( sdf.format ( cal.getTime() ) + " " + CommonUtilities.getOffsetGMT() );
						thisDetail.setSyncTime ( "1900-01-01 12:57:00" );
						thisDetail.setSyncStatus ( "N" );
						thisDetail.setTechnicianGroupID ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getGroupID() );
						thisDetail.setTechnicianPersonID ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getPersonID() );
						thisDetail.setTechnicianUserRoleCode ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getUserRoleCode() );
						thisDetail.setTechnicianUserSubRoleCode ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getUserSubRoleCode() );

						String macAddress = ((TangoApplication) getApplication()).getUnitMacAddress();
						String injectionID = null;
						if ( macAddress != null )
						{

							injectionID = InjectionStationDAO.getID ( getApplicationContext(), macAddress );
						}
						thisDetail.setInjectionStationID_act ( injectionID );
						thisDetail.setTermicideTypeCode_act ( selectedWorkOrder.getTermicideTypeCodePlanned() );

						switch ( newData.getSoilType() )
						{
							case 0:
								thisDetail.setSoilTypeCode_act("S");
								break;
							case 1:
								thisDetail.setSoilTypeCode_act("H");
								break;
							case 2:
								thisDetail.setSoilTypeCode_act("S");
								break;
							case 3:
								thisDetail.setSoilTypeCode_act("L");
								break;
						}
						thisDetail.setSoilTypeRefCode_act(Integer.toString((newData.getSoilType())));
						thisDetail.setTermicideTypeName_act ( newData.getProductApplied() );
						thisDetail.setTermicideTypeCode_act(CommonUtilities.getTermicideTypeByName(getApplicationContext(), newData.getProductApplied()).getTermicideTypeCode());
						thisDetail.setHT_LinearDistanceApplication_act ( CommonUtilities.getDistanceByInjectionCount(thisDetail.getHT_InjectionCount_act(),selectedWorkOrder.getLinearMeasurementUnit()) );
						thisDetail.setHT_InjectorTimer_act ( Float.toString(newData.getInjectorTimer()) );
						thisDetail.setSA_PumpVolume_act ( Float.toString(newData.getHtModePumpVolume()));
						thisDetail.setLinearMeasurementUnit_act  ( selectedWorkOrder.getLinearMeasurementUnit());
						thisDetail.setVolumeMeasurementUnit_act  ( selectedWorkOrder.getVolumeMeasurementUnit() );
						// Null check for GPS location
						double latitude, longitude;
						latitude = longitude = 0.0;
						if ( whereAmI != null )
						{
							latitude = whereAmI.getLatitude();
							longitude = whereAmI.getLongitude();
						}
						thisDetail.setLatitude ( Double.toString ( latitude ) );
						thisDetail.setLongitude ( Double.toString ( longitude ) );

						thisDetail.setMinutesWorkedInSession_act ( Integer.toString(newData.getTimeToComplete()) );
						thisDetail.setWorkorderSlotLocationUsed ( "1" );
						thisDetail.setWorkorderStatusCode ( WorkorderStatus.ONHOLD );


						WorkorderDetailDAO.add ( WorkOrderDetailActivity.this.getApplicationContext(), thisDetail );

						//was successful send delete command
						boolean status = thisService.eraseData();
						//status = status;
					}
					else{
						errorString = newData.getLastErrorMessage();
						error = true;
					}
				}

				if (errorString.equals("SUCCESS")){
					boolean sendSuccess = thisService.sendWorkOrderToTermidorHP ( selectedWorkOrder );
					if (sendSuccess)
					{
						writeBlankRecord(WorkorderStatus.OPEN);

						// get unsynched details, and send to web server
						if(CommonUtilities.checkIfWorkorderChanges(WorkOrderDetailActivity.this.getApplicationContext(),token)){
							CommonUtilities.refreshWorkOrders(WorkOrderDetailActivity.this.getApplicationContext(),token);
						}else{
							CommonUtilities.checkForNewDetails(WorkOrderDetailActivity.this.getApplicationContext(),token);
						}

					}
					else
					{
						error = true;
					}
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
				error = true;
			}

			thisService.deInitialize();

			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			// execution of result of Long time consuming operation
			//sendReceivPB.setVisibility(View.GONE);
			if ((sendReceivPB != null) && sendReceivPB.isShowing()) {
				sendReceivPB.dismiss();
			}

			if (error)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(WorkOrderDetailActivity.this);
				builder.setTitle("Send Workorder");
				if (errorString.equals("SUCCESS")){
					builder.setMessage(getString(R.string.unitSendErrorMessage));
				} else
				{
					builder.setMessage(errorString);
				}
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			else
			{
				if (errorString.equals("SUCCESS")){
					manageButtons ( "OPEN" );
					woStatus.setText("OPEN");
					Toast toast = CreateToast(getResources().getColor(R.color.message_completed), "Workorder Sent", Gravity.TOP);
					toast.show();
				}
				else {
					Toast toast = CreateToast(getResources().getColor(R.color.message_error), errorString, Gravity.TOP);
					toast.show();
				}
				recreate();
			}


		}
		@Override
		protected void onPreExecute()
		{
			sendReceivPB = ProgressDialog.show(WorkOrderDetailActivity.this,null,null);
			sendReceivPB.setContentView(new ProgressBar(WorkOrderDetailActivity.this));

		}
	}
	private class AsyncTaskRunner2 extends AsyncTask<String, Void, String>
	{
		String errorString="SUCCESS";

		boolean onHoldIndicator = false;
		Location whereAmI;
		boolean error = false;
		ProgressDialog sendReceivPB = null;
		ArrayList<WorkOrderDetail> detailList;
		//final TermidorMessageInterface thisService = new CANMessageService();
		final TermidorMessageInterface thisService = new CanMessageServiceStandard();
		//final TermidorMessageInterface thisService = new TestTermidorMessageImpl();
		AsyncTaskRunner2 ( boolean onHold, Location myLocation )
		{
			onHoldIndicator = onHold;
			whereAmI = myLocation;
		}

		@Override
		protected String doInBackground(String... params)
		{
			try
			{

				boolean init = thisService.initialize(((TangoApplication) getApplicationContext()).getUnitMacAddress());
				if(init == false){
					throw new Exception("Unable to connect to Unit");
				}

				WorkOrderDetail thisDetail;
				//int sumInjections = 0;

				newData = thisService.getData();
				// update local db
				if ( newData != null )
				{
					if (newData.getLastErrorMessage().equals("SUCCESS"))
					{

						thisDetail = new WorkOrderDetail ( newData );

						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

						thisDetail.setEntryTime ( sdf.format ( cal.getTime() ) + " " + CommonUtilities.getOffsetGMT() );
						thisDetail.setSyncTime ( "1900-01-01 12:57:00" );
						thisDetail.setSyncStatus ( "N" );
						thisDetail.setTechnicianGroupID ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getGroupID() );
						thisDetail.setTechnicianPersonID ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getPersonID() );
						thisDetail.setTechnicianUserRoleCode ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getUserRoleCode() );
						thisDetail.setTechnicianUserSubRoleCode ( ((TangoApplication) WorkOrderDetailActivity.this.getApplicationContext()).getUser().getUserSubRoleCode() );

						String macAddress = ((TangoApplication) getApplication()).getUnitMacAddress();
						String injectionID = null;
						if ( macAddress != null )
						{
							injectionID = InjectionStationDAO.getID ( getApplicationContext(), macAddress );
						}
						thisDetail.setInjectionStationID_act ( injectionID );


						switch ( newData.getSoilType() )
						{
							case 0:
								thisDetail.setSoilTypeCode_act("S");
								break;
							case 1:
								thisDetail.setSoilTypeCode_act("H");
								break;
							case 2:
								thisDetail.setSoilTypeCode_act("S");
								break;
							case 3:
								thisDetail.setSoilTypeCode_act("L");
								break;
						}

						thisDetail.setSoilTypeRefCode_act(Integer.toString(newData.getSoilType()));
						thisDetail.setTermicideTypeName_act ( newData.getProductApplied() );
						thisDetail.setTermicideTypeCode_act(CommonUtilities.getTermicideTypeByName(getApplicationContext(), newData.getProductApplied()).getTermicideTypeCode());

						thisDetail.setHT_LinearDistanceApplication_act ( CommonUtilities.getDistanceByInjectionCount(thisDetail.getHT_InjectionCount_act(),selectedWorkOrder.getLinearMeasurementUnit()) );
						thisDetail.setHT_InjectorTimer_act ( Float.toString(newData.getInjectorTimer()) );
						thisDetail.setSA_PumpVolume_act ( Float.toString(newData.getHtModePumpVolume()));
						thisDetail.setLinearMeasurementUnit_act  ( selectedWorkOrder.getLinearMeasurementUnit());
						thisDetail.setVolumeMeasurementUnit_act  ( selectedWorkOrder.getVolumeMeasurementUnit() );
						// Null check for GPS location
						double latitude, longitude;
						latitude = longitude = 0.0;
						if ( whereAmI != null )
						{
							latitude = whereAmI.getLatitude();
							longitude = whereAmI.getLongitude();
						}
						thisDetail.setLatitude ( Double.toString ( latitude ) );
						thisDetail.setLongitude ( Double.toString ( longitude ) );

						thisDetail.setMinutesWorkedInSession_act ( Integer.toString(newData.getTimeToComplete()) );
						thisDetail.setWorkorderSlotLocationUsed ( "1" );
						if (newData.getWorkOrderID().equals(selectedWorkOrder.getServiceWorkOrderId()))
						{
							if ( onHoldIndicator ){
								thisDetail.setWorkorderStatusCode ( WorkorderStatus.ONHOLD );
							}else{
								thisDetail.setWorkorderStatusCode ( WorkorderStatus.CLOSED );
							}
							WorkorderDetailDAO.add ( WorkOrderDetailActivity.this.getApplicationContext(), thisDetail );
						}
						else
						{
							thisDetail.setWorkorderStatusCode ( WorkorderStatus.ONHOLD );
							WorkorderDetailDAO.add ( WorkOrderDetailActivity.this.getApplicationContext(), thisDetail );

							if ( onHoldIndicator ){
								writeBlankRecord(WorkorderStatus.ONHOLD);
							}else{
								writeBlankRecord(WorkorderStatus.CLOSED);
							}
						}





						//was successful send delete command
						boolean test = thisService.eraseData();


						thisService.deInitialize();

					}
					else
					{
						errorString = newData.getLastErrorMessage();
						error = true;
					}

				}else{


					if ( onHoldIndicator ){
						writeBlankRecord(WorkorderStatus.ONHOLD);
					}else{
						writeBlankRecord(WorkorderStatus.CLOSED);
					}
				}



				// get unsynched details, and send to web server
				if(CommonUtilities.checkIfWorkorderChanges(WorkOrderDetailActivity.this.getApplicationContext(),token)){
					CommonUtilities.refreshWorkOrders(WorkOrderDetailActivity.this.getApplicationContext(),token);
				}else{
					CommonUtilities.checkForNewDetails(WorkOrderDetailActivity.this.getApplicationContext(),token);
				}
			}
			catch (Exception e)
			{
				thisService.deInitialize();
				e.printStackTrace();
				errorString = e.getMessage().toString();
				error = true;
			}



			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			// execution of result of Long time consuming operation
			if ((sendReceivPB != null) && sendReceivPB.isShowing()) {
				sendReceivPB.dismiss();
			}


			if (error)
			{

				AlertDialog.Builder builder = new AlertDialog.Builder(WorkOrderDetailActivity.this);
				builder.setTitle("Receive Workorder Detail");
				if (errorString.equals("SUCCESS"))
				{
					builder.setMessage(errorString);
				}
				else
				{
					builder.setMessage(errorString);
				}
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			else
			{
				// finish to list view activity on Complete
				if (errorString.equals("SUCCESS")){

				/*if ( ! onHoldIndicator )
                {
                    // do complete stuff
                    finish();
                }*/
/*	            if (showWorkorderStatus().equals("") || showWorkorderStatus().equals("ASSIGNED") || showWorkorderStatus().equals("NEW"))
	            {
	            	hideDetailData();
	            }
	            else
	            {
	            	showDetailData();
	            	updateDetailDataOnScreen();
	            }
                if(CommonUtilities.areAnyWorkordersOpen(getApplicationContext())){
                	btnSendData.setVisibility ( View.GONE );
                	if(CommonUtilities.isThisWorkorderOpen(getApplicationContext(),selectedWorkOrder.getServiceWorkOrderId())){
                		btnComplete.setVisibility(View.VISIBLE);
                	}else{
                		btnOnHold.setVisibility(View.GONE);
                		btnComplete.setVisibility(View.GONE);
                	}
                }else{
                	btnSendData.setVisibility ( View.VISIBLE );
            		btnOnHold.setVisibility(View.GONE);
            		btnComplete.setVisibility(View.GONE);
                } */
					if (onHoldIndicator) {
						Toast toast = CreateToast(getResources().getColor(R.color.message_completed), "Workorder Placed On Hold", Gravity.TOP);
						toast.show();
					}
					else
					{
						Toast toast = CreateToast(getResources().getColor(R.color.message_completed), "Workorder Completed", Gravity.TOP);
						toast.show();
					}
				} else {
					Toast toast = CreateToast(getResources().getColor(R.color.message_completed), errorString, Gravity.TOP);
					toast.show();

				}
			}

			//showWorkorderStatus();
			recreate();
		}
		@Override
		protected void onPreExecute()
		{

			sendReceivPB = ProgressDialog.show(WorkOrderDetailActivity.this,null,null);
			sendReceivPB.setContentView(new ProgressBar(WorkOrderDetailActivity.this));

		}
	}
	class RetrieveDetailTask extends AsyncTask<Object, Void, Integer>
	{
		//boolean error = false;
		ArrayList<WorkOrderDetail> detailList;
		ProgressDialog sendReceivPB = null;

		@Override
		protected void onPostExecute(Integer result)
		{
			if ((sendReceivPB != null) && sendReceivPB.isShowing()) {
				sendReceivPB.dismiss();
			}

			if (showWorkorderStatus().equals("") || showWorkorderStatus().equals("ASSIGNED") || showWorkorderStatus().equals("NEW"))
			{
				hideDetailData();
			}
			else
			{
				showDetailData();
				updateDetailDataOnScreen();
			}


	            /*if (error)
				{
					Toast.makeText(getApplicationContext(), "Invalid login attempt", Toast.LENGTH_SHORT).show();
				}*/
		}

		@Override
		protected void onPreExecute()
		{
			sendReceivPB = ProgressDialog.show(WorkOrderDetailActivity.this,null,null);
			sendReceivPB.setContentView(new ProgressBar(WorkOrderDetailActivity.this));
		}
		@Override
		protected Integer doInBackground(Object... params) {

			if(CommonUtilities.checkIfWorkorderChanges(WorkOrderDetailActivity.this.getApplicationContext(),token)){
				CommonUtilities.refreshWorkOrders(WorkOrderDetailActivity.this.getApplicationContext(),token);
			}else{
				CommonUtilities.checkForNewDetails(WorkOrderDetailActivity.this.getApplicationContext(),token);
			}
			return null;

		}

	}
}
