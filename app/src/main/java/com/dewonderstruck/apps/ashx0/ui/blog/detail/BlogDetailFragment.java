package com.dewonderstruck.apps.ashx0.ui.blog.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.dewonderstruck.apps.ashx0.R;
import com.dewonderstruck.apps.ashx0.databinding.FragmentBlogDetailBinding;
import com.google.android.gms.ads.AdRequest;
import com.dewonderstruck.apps.Config;

import com.dewonderstruck.apps.ashx0.binding.FragmentDataBindingComponent;

import com.dewonderstruck.apps.ashx0.ui.common.PSFragment;
import com.dewonderstruck.apps.ashx0.utils.AutoClearedValue;
import com.dewonderstruck.apps.ashx0.utils.Constants;
import com.dewonderstruck.apps.ashx0.utils.PSDialogMsg;
import com.dewonderstruck.apps.ashx0.viewmodel.blog.BlogViewModel;

public class BlogDetailFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private BlogViewModel blogViewModel;
    private String blogId;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentBlogDetailBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentBlogDetailBinding dataBinding = (FragmentBlogDetailBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_blog_detail, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initViewModels() {

        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            blogId = getActivity().getIntent().getStringExtra(Constants.BLOG_ID);
        }

        if (blogId != null && !blogId.isEmpty()) {
            blogViewModel.setBlogByIdObj(blogId);

            blogViewModel.blogByIdData.observe(this, result -> {

                if (result != null) {
                    if (result.data != null) {
                        switch (result.status) {
                            case SUCCESS:
                                binding.get().setBlog(result.data);
                                break;

                            case ERROR:
                                psDialogMsg.showErrorDialog(getString(R.string.blog_detail__error_message), getString(R.string.app__ok));
                                psDialogMsg.show();
                                break;

                            case LOADING:
                                binding.get().setBlog(result.data);
                                break;
                        }
                    }
                }
            });
        }

    }
}
