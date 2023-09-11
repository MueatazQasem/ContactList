package ca.myandroid.mueataz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ca.myandroid.mueataz.model.Account;
import ca.myandroid.mueataz.model.Customer;

public class MainActivity extends AppCompatActivity {
    private RecyclerView customersView;
    private CustomersAdaptor customersAdaptor;
    private ArrayList<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customersView = findViewById(R.id.customers_view);
        customersView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        customers = new ArrayList<>();

        customersAdaptor = new CustomersAdaptor();
        customersView.setAdapter(customersAdaptor);

        loadCustomers();
    }

    private class CustomerHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private ImageView photoView;
        private TextView balanceView;
        private TextView accountNumberView;
        private ImageView callView;
        private ImageView emailView;
        private Customer customer;

        public CustomerHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name_view);
            balanceView = itemView.findViewById(R.id.balance_view);
            photoView = itemView.findViewById(R.id.photo_view);
            accountNumberView = itemView.findViewById(R.id.account_number_view);

            callView = itemView.findViewById(R.id.call_button);
            callView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customer.getPhone()));
                startActivity(intent);
            });

            emailView = itemView.findViewById(R.id.email_button);
            emailView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + customer.getPhone()));
                startActivity(intent);
            });

            itemView.setOnClickListener(v -> {
                Intent i = new Intent(MainActivity.this, CustomerActivity.class);
                i.putExtra("customer", customer);
                i.putExtra("customers", customers);
                startActivity(i);
                finish();
            });

            itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Deleting")
                        .setMessage("Do you really want to delete the customer " + customer.getName() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteCustomer(customer.getAccountNumber()))
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            });
        }

        private void bindCustomer(Customer customer){
            this.customer = customer;
            nameView.setText(customer.getName());
            balanceView.setText("$" + customer.getBalance());
            photoView.setImageResource(getResources().getIdentifier(customer.getPhoto(), "drawable", getPackageName()));
            accountNumberView.setText(customer.getAccountNumber());
        }
    }

    public boolean deleteCustomer(String accountNumber){
        for(int i = 0; i < customers.size(); i++){
            if(customers.get(i).getAccountNumber().equals(accountNumber)){
                customers.remove(i);
                customersAdaptor.notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public void loadCustomers() {
        customers.clear();
        try {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("customers")) {
                customers.addAll((ArrayList<Customer>) intent.getSerializableExtra("customers"));
            } else {
                JSONArray customers = getCustomers();
                for(int i = 0; i < customers.length(); i++){
                    JSONObject customer = customers.getJSONObject(i);
                    this.customers.add(new Customer(new Account(customer.getString("accountNumber"), customer.getString("openDate"), customer.getString("balance")), customer.getString("name"), customer.getString("family"), customer.getString("phone"), customer.getString("sin"), customer.getString("photoFileName")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getCustomers() throws IOException, JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.customers);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new JSONArray(new String(buffer, "UTF-8"));
    }

    private class CustomersAdaptor extends RecyclerView.Adapter<CustomerHolder> {
        public CustomersAdaptor() {
        }

        @Override
        public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.customer, parent, false);
            return new CustomerHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomerHolder holder, int position) {
            holder.bindCustomer(customers.get(position));
        }

        @Override
        public int getItemCount() {
            return customers.size();
        }
    }
}