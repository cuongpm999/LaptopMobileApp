package vn.ptit.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.ptit.R;

public class UserFragmentCredit extends Fragment {
    private EditText etNumber, etType, etDate;
    public UserFragmentCredit() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_credit,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNumber = view.findViewById(R.id.etNumber);
        etType = view.findViewById(R.id.etType);
        etDate = view.findViewById(R.id.etDate);
    }

    public EditText getEtNumber() {
        return etNumber;
    }

    public EditText getEtType() {
        return etType;
    }

    public EditText getEtDate() {
        return etDate;
    }
}
