package ca.myandroid.mueataz;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import ca.myandroid.mueataz.model.Account;
import ca.myandroid.mueataz.model.Customer;
public class CustomerActivity extends AppCompatActivity {
    private EditText accountNumberEditText, openDateEditText, balanceEditText,  nameEditText, familyEditText, phoneEditText, sinEditText;
    private ImageView customerImageView;
    private ArrayList<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        customers = new ArrayList<>();

        customerImageView = findViewById(R.id.customerImageView);
        accountNumberEditText = findViewById(R.id.accountNumberEditText);
        openDateEditText = findViewById(R.id.openDateEditText);
        balanceEditText = findViewById(R.id.balanceEditText);
        nameEditText = findViewById(R.id.nameEditText);
        familyEditText = findViewById(R.id.familyEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        sinEditText = findViewById(R.id.sinEditText);

        findViewById(R.id.addButton).setOnClickListener(v -> {
            String accountNumber = accountNumberEditText.getText().toString();
            String openDate = openDateEditText.getText().toString();
            String balance = balanceEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String family = familyEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String sin = sinEditText.getText().toString();

            //Checking to see if all data has been filled
            if (accountNumber.isEmpty() || openDate.isEmpty() || balance.isEmpty() || name.isEmpty() || family.isEmpty() || phone.isEmpty() || sin.isEmpty()) {
                return;
            }

            if (isAccountNumberUnique(accountNumber)) {
                customers.add(new Customer(new Account(accountNumber, openDate, balance), name, family, phone, sin, "customer.png"));
            }

            clearFields();
        });

        findViewById(R.id.findButton).setOnClickListener(v -> {
            String accountNumber = accountNumberEditText.getText().toString();
            Customer customer = findCustomerByAccountNumber(accountNumber);
            if (customer != null) {
                populateFields(customer);
            } else {
                clearFields();
            }
        });

        findViewById(R.id.removeButton).setOnClickListener(v -> {
            String accountNumber = accountNumberEditText.getText().toString();
            Customer customer = findCustomerByAccountNumber(accountNumber);
            if (customer != null) {
                customers.remove(customer);
                clearFields();
            }
        });

        findViewById(R.id.updateButton).setOnClickListener(v -> {
            String accountNumber = accountNumberEditText.getText().toString();
            Customer oldCustomer = findCustomerByAccountNumber(accountNumber);

            if (oldCustomer != null) {
                String openDate = openDateEditText.getText().toString();
                String balance = balanceEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String family = familyEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String sin = sinEditText.getText().toString();

                customers.remove(oldCustomer);
                customers.add(new Customer(new Account(accountNumber, openDate, balance), name, family, phone, sin, oldCustomer.getPhoto() + ".png"));

                clearFields();
            }
        });

        findViewById(R.id.clearButton).setOnClickListener(v -> clearFields());

        findViewById(R.id.showAllButton).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerActivity.this, MainActivity.class);
            intent.putExtra("customers", new ArrayList<>(customers));
            startActivity(intent);
        });

        //Loading customers from main activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("customers")) {
            customers.addAll((ArrayList<Customer>) intent.getSerializableExtra("customers"));
        }

        //Loading clicked customer from main activity
        if (intent != null && intent.hasExtra("customer")) {
            populateFields((Customer) intent.getSerializableExtra("customer"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CustomerActivity.this, MainActivity.class);
        i.putExtra("customers", customers);
        startActivity(i);
        finish();
    }

    private boolean isAccountNumberUnique(String accountNumber) {
        for (Customer customer : customers) {
            if (customer.getAccountNumber().equals(accountNumber)) {
                return false;
            }
        }
        return true;
    }

    private Customer findCustomerByAccountNumber(String accountNumber) {
        for (Customer customer : customers) {
            if (customer.getAccountNumber().equals(accountNumber)) {
                return customer;
            }
        }
        return null;
    }

    private void populateFields(Customer customer) {
        accountNumberEditText.setText(customer.getAccountNumber());
        openDateEditText.setText(customer.getOpenDate());
        balanceEditText.setText(customer.getBalance());
        nameEditText.setText(customer.getName());
        familyEditText.setText(customer.getFamily());
        phoneEditText.setText(customer.getPhone());
        sinEditText.setText(customer.getSin());
        customerImageView.setImageResource(getResources().getIdentifier(customer.getPhoto(), "drawable", getPackageName()));;
    }

    private void clearFields() {
        accountNumberEditText.setText("");
        openDateEditText.setText("");
        balanceEditText.setText("");
        nameEditText.setText("");
        familyEditText.setText("");
        phoneEditText.setText("");
        sinEditText.setText("");
        customerImageView.setImageResource(R.drawable.customer);
    }
}