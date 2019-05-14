package ots.hr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;


    public class LeaveFragment extends Fragment {

        LeavesAdapter leavesAdapter;

        public LeaveFragment() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onStart() {
            super.onStart();
            fetchLeaves();
        }

        private void fetchLeaves(){
            FetchLeavesTask fetchLeavesTask = new FetchLeavesTask(leavesAdapter);
            fetchLeavesTask.execute();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            leavesAdapter = new LeavesAdapter(getActivity(), new ArrayList<Leave>());
            ListView leavesListView = (ListView) rootView.findViewById(R.id.list_info);
            leavesListView.setAdapter(leavesAdapter);

            return rootView;
        }

    }



















