package ly.kite.journey;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ly.kite.cliqueshare.CliqueshareUtils;
import ly.kite.R;

import static ly.kite.journey.creation.AProductCreationFragment.CLIQUESHARE_FRAGMENT_CODE;

public class CliqueShareImageSource extends AImageSource{


    public CliqueShareImageSource()
    {
        super( R.color.image_source_background_device,
                R.drawable.cliquesharemenuicon,
                R.string.cliqueshare,
                R.id.add_image_from_cliqueshare,
                R.string.cliqueshare);
    }

    @Override
    public boolean isAvailable(Context context) {
        return true;
    }

    @Override
    public void onPick(Fragment targetfrag, int addedAssetCount, boolean supportsMultiplePacks, int packSize, int maxImageCount) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("multiselect",supportsMultiplePacks);

        FragmentTransaction ft = targetfrag.getActivity().getFragmentManager().beginTransaction();
        CliqueshareUtils fragment = new CliqueshareUtils();
        fragment.setTargetFragment(targetfrag, CLIQUESHARE_FRAGMENT_CODE);
        fragment.setArguments(bundle);
        ft.addToBackStack(fragment.getClass().getName());

        ft.add(R.id.fragment_container, fragment, targetfrag.getTag());
        ft.commit();

    }

    @Override
    public void getAssetsFromPickerResult(Activity activity, Intent data, IAssetConsumer assetConsumer) {



    }
}
