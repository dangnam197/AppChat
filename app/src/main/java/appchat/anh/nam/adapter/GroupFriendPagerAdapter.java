package appchat.anh.nam.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupFriendPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> listFragment = new ArrayList<>();

    public GroupFriendPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    public void addFragment(Fragment fragment){
        if(fragment!=null)
            listFragment.add(fragment);
    }
}
