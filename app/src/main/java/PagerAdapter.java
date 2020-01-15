import com.example.goa.fragments.Tab1;
import com.example.goa.fragments.Tab2;
import com.example.goa.fragments.Tab3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int number;

    public PagerAdapter(@NonNull FragmentManager fm, int number) {
        super(fm);
        this.number = number;
    }

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int number) {
        super(fm, behavior);
        this.number = number;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
        case 0:
        Tab1 tab1=new Tab1();
        return Tab1;
        case 1:
        Tab2 tab2 = new Tab2();
        return( Tab2);
        case 2:
        Tab3 tab3 = new Tab3();
        return(Tab3);
        default:
        return null;}
    }

    @Override
    public int getCount() {
        return number;
    }
}