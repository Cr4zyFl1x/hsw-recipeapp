package nrw.florian.cookbook.fragment.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.databinding.FragmentRecipeTagViewBinding;
import nrw.florian.cookbook.db.recipeproperty.RecipePropertyEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeTagViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeTagViewFragment extends Fragment {

    private static final String RECIPE_PROPERTY = "recipeProperty";

    private RecipePropertyEntity recipePropertyEntity;
    private FragmentRecipeTagViewBinding binding;


    public static RecipeTagViewFragment newInstance(final RecipePropertyEntity recipePropertyEntity)
    {
        RecipeTagViewFragment fragment = new RecipeTagViewFragment();
        Bundle args = new Bundle();

        args.putSerializable(RECIPE_PROPERTY, recipePropertyEntity);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipePropertyEntity = getArguments()
                    .getSerializable(RECIPE_PROPERTY, RecipePropertyEntity.class);
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

        binding.tagContainer.setText(recipePropertyEntity.getProperty().getString(requireContext()));
    }


    public RecipePropertyEntity getRecipePropertyEntity()
    {
        return recipePropertyEntity;
    }
}