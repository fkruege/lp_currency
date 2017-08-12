package com.franctan.lonelyplanetcurrencyguide.test_support;


import com.franctan.lonelyplanetcurrencyguide.TestApp;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

public class MockTestRunner extends AndroidJUnitRunner {
//  @Override
//  public void finish(int resultCode, Bundle results) {
//    InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase
//        (MockDatabaseApiModule.DATABASE_NAME);
//    super.finish(resultCode, results);
//  }

  @Override
  public Application newApplication(ClassLoader cl, String className, Context context)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return super.newApplication(cl, TestApp.class.getName(), context);
  }
}