package nrw.florian.cookbook.fragment.list;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.databinding.FragmentRecipeTagViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeTagViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeTagViewFragment extends Fragment {

    private static final String TAG_STRING = "tagString";
    private static final String TAG_COLOR_ID = "tagColorId";

    private String tagString;
    private int tagColorId;
    private FragmentRecipeTagViewBinding binding;


    public static RecipeTagViewFragment newInstance(final String tagName, final int tagColorId)
    {
        RecipeTagViewFragment fragment = new RecipeTagViewFragment();
        Bundle args = new Bundle();

        args.putString(TAG_STRING, tagName);
        args.putInt(TAG_COLOR_ID, tagColorId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tagString = getArguments().getString(TAG_STRING);
            tagColorId = getArguments().getInt(TAG_COLOR_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentRecipeTagViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        GradientDrawable tg = (GradientDrawable) binding.tagContainer.getBackground();
        tg.setColor(requireContext().getColor(tagColorId));
        binding.tagContainer.setText(tagString);
    }
}