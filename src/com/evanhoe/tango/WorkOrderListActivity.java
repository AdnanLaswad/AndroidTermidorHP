package com.evanhoe.tango;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.evanhoe.tango.dao.WorkOrderDAO;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.WorkOrderViewEntryItem;
import com.evanhoe.tango.objs.WorkOrderViewItem;
import com.evanhoe.tango.objs.WorkOrderViewSectionItem;
import com.evanhoe.tango.utils.CommonUtilities;
import com.evanhoe.tango.utils.ListAdapter;
import com.evanhoe.tango.utils.SqlLiteDbHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorkOrderListActivity extends OptionsMenuActivity {
	int inProgressSectionLocation = -1;
	int newSectionLocation = -1;
	int onHoldSectionLocation = -1;
	int completedSectionLocation = -1;
	ListView lvwWorkOrdersInProgress;
	ArrayList<WorkOrder> workOrders = null;
	ArrayList<WorkOrderViewItem> items = new ArrayList<WorkOrderViewItem>();
	ListAdapter lAdapter = null;
int chker=0;
	ImageButton filterButton;
     String token="";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//overridePendingTransition ( R.anim.fadein, R.anim.fadeout );
		//CommonUtilities.refreshWorkOrders2(WorkOrderListActivity.this.getApplicationContext(),token);
		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			setTheme(R.style.AppTheme);
			chker=1;

		}
		else{
			setTheme(R.style.AppTheme1);

		}

		setContentView(R.layout.activity_work_order_list);
		Button staging=(Button)findViewById(R.id.stag2);
		if(chker==0){
			staging.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = getSharedPreferences(
				"userdata", Context.MODE_PRIVATE);
		token=sharedPreferences.getString("token","");
		items.clear();
		lvwWorkOrdersInProgress = (ListView) findViewById(R.id.lvwWorkOrdersInProgress);
		lvwWorkOrdersInProgress.setAdapter(null);
		workOrders = new ArrayList<WorkOrder>();
		lAdapter = new ListAdapter(this,items);


		lvwWorkOrdersInProgress.setAdapter(lAdapter);

		lvwWorkOrdersInProgress.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(!((WorkOrderViewEntryItem) items.get(position)).isSection()){
					WorkOrder selectedWorkOrder = workOrders.get(position);
					Intent i = new Intent(getApplicationContext(), WorkOrderDetailActivity.class);
					i.putExtra("workOrder",selectedWorkOrder);
					startActivity(i);
				}

			}
		});

		checkDBexists();
		try
		{
			//loadWorkOrdersToDB();

			new RetrieveWOListTask().execute();



		}
		catch ( Exception e )
		{
			e.printStackTrace();
			//Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	public boolean onPrepareOptionsMenu(Menu menu) {

		//menu.removeItem(R.id.actionSettings);
		menu.removeItem(R.id.actionWorkOrderList);
		//menu.removeItem(R.id.actionLogOut);


		if(!((TangoApplication) this.getApplication()).getUser().canDoTraining()){
			menu.removeItem(R.id.actionTraining);
			((TangoApplication) WorkOrderListActivity.this.getApplication()).setIsTrainingMode(false);
		}else{

			MenuItem trainingMenuItem = menu.findItem(R.id.actionTraining);

			if(((TangoApplication) this.getApplication()).getIsTrainingMode()){
				trainingMenuItem.setTitle(R.string.work);
			}else{
				trainingMenuItem.setTitle(R.string.training);
			}

		}


		return true;
	}

	/**
	 * get workorders, isTrainingFilter set to "Y" = get only training records
	 * isTrainingFilter set to "N" = get only non-training records
	 * isTrainingFilter set to "A" = get all records
	 * @param isTrainingFilter
	 */
	private void loadWorkOrdersToScreen(String isTrainingFilter) {
		ArrayList<WorkOrder> workOrdersNew = new ArrayList<WorkOrder>();
		ArrayList<WorkOrder> workOrdersInProgress = new ArrayList<WorkOrder>();
		ArrayList<WorkOrder> workOrdersCompleted = new ArrayList<WorkOrder>();
		ArrayList<WorkOrder> workOrdersOnHold = new ArrayList<WorkOrder>();


		int numberOfSections=0;
		String status ="nada";

		//workOrders = WorkOrderDAO.getAll ( this.getApplicationContext() );
		if(isTrainingFilter.equalsIgnoreCase("Y")){
			workOrders = WorkOrderDAO.getAll ( this.getApplicationContext(),"Y" );
		}else if(isTrainingFilter.equalsIgnoreCase("N")){
			workOrders = WorkOrderDAO.getAll ( this.getApplicationContext(),"N" );
		}else{
			workOrders = WorkOrderDAO.getAll ( this.getApplicationContext() );
		}

		for ( WorkOrder thisWorkOrder : workOrders )
		{
			status = CommonUtilities.getWorkOrderStatus(WorkOrderListActivity.this.getApplicationContext(), thisWorkOrder.getServiceWorkOrderId());
			switch(status)
			{
				case "OPEN":
					workOrdersInProgress.add(thisWorkOrder);
					break;
				case "COMPLETED":
					workOrdersCompleted.add(thisWorkOrder);
					break;
				case "ON HOLD":
					workOrdersOnHold.add(thisWorkOrder);
					break;
				case "ASSIGNED":
					workOrdersNew.add(thisWorkOrder);
					break;
				default:
					workOrdersNew.add(thisWorkOrder);
					break;
			}

		}
		items.clear();
		workOrders.clear();
		if (workOrdersInProgress.size() > 0)
		{
			items.add(new WorkOrderViewSectionItem("In Progress"));
			workOrders.add(null);
		}
		for ( WorkOrder thisWorkOrder : workOrdersInProgress)
		{
			String address="";
			String workOrderNum = "";
			if(thisWorkOrder.getDemo().equalsIgnoreCase("Y")){
				address = "-- Training --\n";

			}
			address = address + thisWorkOrder.getAddressLine1() + " " + thisWorkOrder.getCity() + ", " + thisWorkOrder.getStateProvinceCode();
			workOrderNum = thisWorkOrder.getServiceManagementWorkorderId();
			items.add(new WorkOrderViewEntryItem(workOrderNum, address, "IN PROGRESS"));
		}
		workOrders.addAll(workOrdersInProgress);

		if (workOrdersOnHold.size() > 0)
		{
			items.add(new WorkOrderViewSectionItem("On Hold"));
			workOrders.add(null);
		}
		for ( WorkOrder thisWorkOrder : workOrdersOnHold )
		{
			String address="";
			String workOrderNum = "";
			if(thisWorkOrder.getDemo().equalsIgnoreCase("Y")){
				address = "-- Training --\n";

			}
			address = address + thisWorkOrder.getAddressLine1() + " " + thisWorkOrder.getCity() + ", " + thisWorkOrder.getStateProvinceCode();
			workOrderNum = thisWorkOrder.getServiceManagementWorkorderId();
			items.add(new WorkOrderViewEntryItem(workOrderNum, address, "ON HOLD"));
		}
		workOrders.addAll(workOrdersOnHold);
		if (workOrdersNew.size() > 0)
		{
			items.add(new WorkOrderViewSectionItem("New"));
			workOrders.add(null);
		}
		for ( WorkOrder thisWorkOrder : workOrdersNew)
		{
			String address="";
			String workOrderNum = "";
			if(thisWorkOrder.getDemo().equalsIgnoreCase("Y")){
				address = "-- Training --\n";

			}
			address = address + thisWorkOrder.getAddressLine1() + " " + thisWorkOrder.getCity() + ", " + thisWorkOrder.getStateProvinceCode();
			workOrderNum = thisWorkOrder.getServiceManagementWorkorderId();
			items.add(new WorkOrderViewEntryItem(workOrderNum, address, "NEW"));
		}
		workOrders.addAll(workOrdersNew);
		if (workOrdersCompleted.size() > 0)
		{
			items.add(new WorkOrderViewSectionItem("Completed"));
			workOrders.add(null);
		}
		for ( WorkOrder thisWorkOrder : workOrdersCompleted)
		{
			String address="";
			String workOrderNum = "";
			if(thisWorkOrder.getDemo().equalsIgnoreCase("Y")){
				address = "-- Training --\n";

			}
			address = address + thisWorkOrder.getAddressLine1() + " " + thisWorkOrder.getCity() + ", " + thisWorkOrder.getStateProvinceCode();
			workOrderNum = thisWorkOrder.getServiceManagementWorkorderId();
			items.add(new WorkOrderViewEntryItem(workOrderNum, address, "COMPLETED"));
		}
		workOrders.addAll(workOrdersCompleted);
	}


	private void checkDBexists() {

		File database = getApplicationContext().getDatabasePath("TG.sqlite");

		if (!database.exists()) {
			// Database does not exist so copy it from assets here
			Log.i("Database", "Not Found");

			SqlLiteDbHelper db = new SqlLiteDbHelper(getApplicationContext());
			try {
				db.CopyDataBaseFromAsset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i("Database", "Exception");
				e.printStackTrace();
			}

		} else {
			Log.i("Database", "Found");
		}
	}

	public void logout(View view) {
		Toast.makeText(getApplicationContext(),"log out",Toast.LENGTH_LONG).show();
		//return;
	}
	class RetrieveWOListTask extends AsyncTask<Object, Void, Integer>
	{
		boolean error = false;
		ProgressDialog sendReceivPB = null;
		@Override
		protected void onPostExecute(Integer result)
		{
			// execution of result of Long time consuming operation

			boolean isTrainingMode = (((TangoApplication) WorkOrderListActivity.this.getApplication()).getIsTrainingMode());
			if(isTrainingMode){
				getActionBar().setTitle(R.string.training_work_order_list);
				loadWorkOrdersToScreen("Y");
			}else{
				getActionBar().setTitle(R.string.work_order_list);
				loadWorkOrdersToScreen("N");
			}

			lAdapter.notifyDataSetChanged();
			sendReceivPB.dismiss();



			if(items.size() < 1){
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(WorkOrderListActivity.this);
				dlgAlert.setMessage("No Work Orders Available");
				dlgAlert.setTitle("Status");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
			}
		}

		@Override
		protected void onPreExecute()
		{
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog

			//sendReceivPB = (ProgressBar)findViewById(R.id.progressBar1);
			//sendReceivPB.setVisibility(View.VISIBLE);

			sendReceivPB = ProgressDialog.show(WorkOrderListActivity.this,null,null);
			sendReceivPB.setContentView(new ProgressBar(WorkOrderListActivity.this));

		}
		@Override
		protected Integer doInBackground(Object... params) {

			// TODO Auto-generated method stub
			//CommonUtilities.syncTheUnsynced(WorkOrderListActivity.this.getApplicationContext());
			//loadWorkOrdersToDB();
			// when need to refresh data base after every login
			//CommonUtilities.refreshWorkOrders(WorkOrderListActivity.this.getApplicationContext(),token);

			if (CommonUtilities.checkIfWorkorderChanges(WorkOrderListActivity.this.getApplicationContext(), token)) {
				CommonUtilities.refreshWorkOrders(WorkOrderListActivity.this.getApplicationContext(), token);
			} else {
				CommonUtilities.checkForNewDetails(WorkOrderListActivity.this.getApplicationContext(), token);
			}


			//CommonUtilities.checkIfWorkorderChanges(WorkOrderListActivity.this.getApplicationContext());

			return 0;

		}

	}
}
