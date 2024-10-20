package com.ktpm1.restaurant.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.adapters.CatalogAdapter;
import com.ktpm1.restaurant.apis.CatalogApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.fragments.homeofs.SuggestionsFragment;
import com.ktpm1.restaurant.listeners.RecyclerTouchListener;
import com.ktpm1.restaurant.models.Catalog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment{
    private RecyclerView rcvCatalogResults;
    private ChipGroup chipGroupCatalogs;
    private TextView tvNotFindCatalog;
    private EditText edtSearchCatalog;
    private List<Catalog> catalogList;
    private CatalogAdapter catalogAdapter;
    private OnCatalogSelectedListener callback;

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        init(view);

        edtSearchCatalog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString();
                fetchCatalogs(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        rcvCatalogResults.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rcvCatalogResults, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Catalog catalogSelected = catalogList.get(position);
                callback.onCatalogSelected(catalogSelected.getId());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void init(View view) {
        rcvCatalogResults = view.findViewById(R.id.rc_searchCatalogResults);
        chipGroupCatalogs = view.findViewById(R.id.chip_groupCatalogs);
        tvNotFindCatalog = view.findViewById(R.id.tv_not_find_catalog);
        edtSearchCatalog = view.findViewById(R.id.edt_search_catalog);
        catalogList = new ArrayList<>();
        catalogAdapter = new CatalogAdapter(catalogList);
        rcvCatalogResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCatalogResults.setAdapter(catalogAdapter);
        fetchCatalogs("");
    }

    public interface OnCatalogSelectedListener {
        void onCatalogSelected(Long catalogId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCatalogSelectedListener) {
            callback = (OnCatalogSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFoodSelectedListener");
        }
    }

    private void fetchCatalogs(String keyword) {
        CatalogApi catalogApi = ApiClient.getClient().create(CatalogApi.class);
        Call<List<Catalog>> call = catalogApi.getAllCatalogs(keyword);
        call.enqueue(new Callback<List<Catalog>>() {
            @Override
            public void onResponse(Call<List<Catalog>> call, Response<List<Catalog>> response) {
                if (response.isSuccessful()) {
                    List<Catalog> catalogs = response.body();
                    if (catalogs != null && !catalogs.isEmpty()) {
                        tvNotFindCatalog.setVisibility(View.GONE);
                        catalogList.clear();
                        catalogList.addAll(catalogs);
                        catalogAdapter.notifyDataSetChanged();
                    } else {
                        tvNotFindCatalog.setVisibility(View.VISIBLE);
                        catalogList.clear();
                        catalogAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Catalog>> call, Throwable t) {
                tvNotFindCatalog.setVisibility(View.VISIBLE);
            }
        });
    }
}