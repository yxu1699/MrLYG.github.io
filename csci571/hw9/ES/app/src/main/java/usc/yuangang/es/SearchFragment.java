package usc.yuangang.es;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import usc.yuangang.es.adapter.SearchVP2Adapter;
import usc.yuangang.es.viewmodel.SearchViewModel;


public class SearchFragment extends Fragment {

    private SearchVP2Adapter searchVP2Adapter;
    private EditText keyword;
    private EditText distance;
    private Spinner category;
    private EditText location;

    private Switch autoCheck;

    private SearchViewModel searchViewModel;


    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSwitchToFragment(int position, Bundle data);
    }

    public SearchFragment() {
    }

    public void initSpinner(View view){
        Spinner spinner = view.findViewById(R.id.spinner);
        TextView selectedItemText = view.findViewById(R.id.selected_item_text);

        String[] items = new String[]{"All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"};

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                selectedItemText.setText(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init spinner
        this.initSpinner(view);

        keyword = view.findViewById(R.id.keyword_input);
        distance = view.findViewById(R.id.distance_input);
        category = view.findViewById(R.id.spinner);
        location = view.findViewById(R.id.location_input);
        autoCheck = view.findViewById(R.id.my_switch);

        // check or uncheck auto-check ---> set location
        autoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Toast.makeText(getActivity(), "autoCheck!", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
        //


        Button clearBtn = view.findViewById(R.id.btn_clear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.setText("");
                distance.setText("10");
                category.setSelection(0);
                location.setText("");
                autoCheck.setChecked(false);
            }
        });

        Button searchBtn = view.findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 按钮点击后的操作
                String keywordTxt = keyword.getText().toString();
                String distanceTxt = distance.getText().toString();
                String categoryTxt = category.getSelectedItem().toString();
                String locationTxt = location.getText().toString();

                searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
                searchViewModel.setKeyword(keywordTxt);
                searchViewModel.setDistance(distanceTxt);
                searchViewModel.setCategory(categoryTxt);
                searchViewModel.setLocation(locationTxt);
                searchViewModel.setAutoCheck(autoCheck.isChecked());

                Toast.makeText(getActivity(), "All info !"+
                        keywordTxt+ distanceTxt+categoryTxt+locationTxt, Toast.LENGTH_SHORT).show();

                // check item not null
                List<String> waitCheckFields = new ArrayList<String>();
                waitCheckFields.add(keywordTxt);
                waitCheckFields.add(distanceTxt);
                waitCheckFields.add(categoryTxt);
                if (!autoCheck.isChecked()){
                    waitCheckFields.add(locationTxt);
                }
                if (checkFieldNotEmpty(waitCheckFields)){
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).vp2.setCurrentItem(1,true);
                    }
                }else {
                    Log.d("FieldCheck", "Please fill all fields");
                    ConstraintLayout constraintLayout = view.findViewById(R.id.snackbar_layout);
                    Snackbar snackbar = Snackbar.make(constraintLayout, "Please fill all fields", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    private boolean checkFieldNotEmpty(List<String> list){
        for (String text : list) {
            if(TextUtils.isEmpty(text)){
                return false;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }





}
