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

    public ImageView intro_imageView;
    public TextView intro_textView;
    public TextView intro_textView2;

    public int pageNumber;

    static final String FACE_STRING = "Welcome!";
    static final String FACE_STRING2 = "tap to continue";
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
        intro_imageView = view.findViewById(R.id.page_iv);
        intro_textView = view.findViewById(R.id.page_tv);
        intro_textView2 = view.findViewById(R.id.page_tv2);

        loadPageImage(pageNumber);
        intro_textView.setText(FACE_STRING);
        intro_textView2.setText(FACE_STRING2);

        intro_imageView.setOnClickListener(v -> startMainActivity());
        return view;
    }

    private void loadPageImage(int page) {
        switch (page) {
            case 0:
                intro_imageView.setImageResource(R.drawable.intro_page1);
                break;
            case 1:
                intro_imageView.setImageResource(R.drawable.intro_page2);
                break;
            case 2:
                intro_imageView.setImageResource(R.drawable.intro_page3);
                break;
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}

