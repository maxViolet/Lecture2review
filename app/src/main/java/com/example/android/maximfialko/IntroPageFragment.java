package com.example.android.maximfialko;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroPageFragment extends Fragment {

    public ImageView imageView;
    public TextView textView;

    public int pageNumber;

    static final String FACE_STRING = "Welcome!";
    static final String PAGE_NUMBER = "page_number";

    static IntroPageFragment newInstance(int page) {
        IntroPageFragment introPage_fragment = new IntroPageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(PAGE_NUMBER, page);
        introPage_fragment.setArguments(arguments);

        return introPage_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().getInt(PAGE_NUMBER) != 0) {
            pageNumber = getArguments().getInt(PAGE_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro_fragment, null);
        imageView = (ImageView) view.findViewById(R.id.page_iv);
        textView = (TextView) view.findViewById(R.id.page_tv);

        loadPageImage(pageNumber);
        textView.setText(FACE_STRING);

        imageView.setOnClickListener(v -> startMainActivity());

        return view;
    }

    private void loadPageImage(int page) {
        switch (page) {
            case 0:
                imageView.setImageResource(R.drawable.intro_page1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.intro_page2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.intro_page3);
                break;
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}

