package com.example.tp_retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemActionListener {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ApiService apiService;
    private boolean useXml = false; // Tracks the selected data format
    private Spinner formatToggleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        formatToggleSpinner = findViewById(R.id.formatToggleSpinner);
        if (formatToggleSpinner != null) {
            setupFormatToggle();
        }

        // Initialize API service with the default JSON setting
        apiService = RetrofitClient.getRetrofit(useXml).create(ApiService.class);

        // Fetch items from the backend
        fetchItems();
    }

    private void setupFormatToggle() {
        formatToggleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                useXml = position == 1; // 0 = JSON, 1 = XML
                // Recreate API service with updated format selection
                apiService = RetrofitClient.getRetrofit(useXml).create(ApiService.class);
                fetchItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void fetchItems() {
        apiService.getItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();
                    if (items.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No items available", Toast.LENGTH_SHORT).show();
                    } else {
                        // Pass the listener to the adapter
                        adapter = new ItemAdapter(items, MainActivity.this, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Implement the OnItemActionListener methods
    @Override
    public void onModifyClick(Item item) {
        // Handle modify action
        editItem(item.getId(), item); // Assume Item has getId method to retrieve its ID
    }

    @Override
    public void onDeleteClick(Item item) {
        // Handle delete action
        deleteItem(item.getId());
    }

    public void addItem(Item item) {
        apiService.addItem(item).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    fetchItems();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editItem(int id, Item item) {
        apiService.updateItem(id, item).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    fetchItems();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteItem(int id) {
        apiService.deleteItem(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchItems();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
