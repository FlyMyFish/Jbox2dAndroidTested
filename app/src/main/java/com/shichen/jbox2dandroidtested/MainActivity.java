package com.shichen.jbox2dandroidtested;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by shichen on 2017/11/10.
 *
 * @author shichen 754314442@qq.com
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simulateWorld =findViewById(R.id.world_simulate);
    }

    private SimulateWorld simulateWorld;

    public void removeOne(View view){
        simulateWorld.removeOne();
    }

    public void addNewOne(View view){
        simulateWorld.addNewOne();
    }
}
