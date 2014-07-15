package com.guimi;

import com.guimi.entities.PersonInfo;
import com.guimi.myviews.UploadDialog;
import com.guimi.sqlite.ConnectServer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class SignupActivity extends Activity {
	
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserSignupTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mPhoneNum;
	private String mPassword;
	private String mName;
	private String mConfirmPassword;
	private ConnectServer cs;

	// UI references.
	private EditText mPhoneNumView;
	private EditText mPasswordView;
	private EditText mConfirmView;
	private EditText mNameView;
	private View mSignupFormView;
	private View mSignupStatusView;
	private TextView mSignupStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_signup);

		cs = ConnectServer.getInstance();

		// Set up the login form.
		mPhoneNum = getIntent().getStringExtra(EXTRA_EMAIL);
		mPhoneNumView = (EditText) findViewById(R.id.phone_num);
		mPhoneNumView.setText(mPhoneNum);

		mNameView = (EditText) findViewById(R.id.name);
		mConfirmView = (EditText) findViewById(R.id.confirm_password);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptSignup();
							return true;
						}
						return false;
					}
				});

		mSignupFormView = findViewById(R.id.signup_form);
		mSignupStatusView = findViewById(R.id.signup_status);
		mSignupStatusMessageView = (TextView) findViewById(R.id.signup_status_message);

		findViewById(R.id.sign_up_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptSignup();
					}
				});
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptSignup() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mPhoneNumView.setError(null);
		mPasswordView.setError(null);
		mNameView.setError(null);
		mConfirmView.setError(null);

		// Store values at the time of the login attempt.
		mPhoneNum = mPhoneNumView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mName = mNameView.getText().toString();
		mConfirmPassword = mConfirmView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid confirm password.
		if (TextUtils.isEmpty(mConfirmPassword)) {
			mConfirmView.setError("这里还没有输0m0！");
			focusView = mPasswordView;
			cancel = true;
		} else if (!mConfirmPassword.equals(mPassword)) {
			mConfirmView.setError("和上面的密码输入不一致哎。。。");
			focusView = mConfirmView;
			cancel = true;
		}

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError("密码还没输0m0！");
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 6 || mPassword.length() > 12) {
			mPasswordView.setError("密码要在6-12个字之间哦~");
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid name.
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError("昵称还没有填写QmQ！");
			focusView = mNameView;
			cancel = true;
		} else if (mName.length() > 12) {
			mNameView.setError("昵称长度不能超过12个字0m0！");
			focusView = mNameView;
			cancel = true;
		}

		// Check for a valid phone number.
		if (TextUtils.isEmpty(mPhoneNum)) {
			mPhoneNumView.setError("手机号还没输呢0m0！");
			focusView = mPhoneNumView;
			cancel = true;
		} else if (mPhoneNum.length() < 11) {
			mPhoneNumView.setError("这个好像不是手机号啊。。。");
			focusView = mPhoneNumView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mSignupStatusMessageView
					.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserSignupTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mSignupStatusView.setVisibility(View.VISIBLE);
			mSignupStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSignupStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mSignupFormView.setVisibility(View.VISIBLE);
			mSignupFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSignupFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mSignupStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserSignupTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			// result=0, sign up succeed;result = -1, sign up failed
			int result = cs.SignUp(mPhoneNum, mPassword, mName);

			// TODO: register the new account here.
			return result;
		}

		@Override
		protected void onPostExecute(final Integer result) {
			mAuthTask = null;
			showProgress(false);

			if (result == 0) {
				Toast.makeText(SignupActivity.this, "注册成功！快去登陆吧！",
						Toast.LENGTH_SHORT).show();
				SharedPreferences userInfo = getSharedPreferences("user_info",
						MODE_PRIVATE);
				Editor editor = userInfo.edit();
				editor.putString("signedUserId", mPhoneNumView.getText().toString());
				editor.putString("signedUserName", mNameView.getText().toString());
				editor.putString("signedPassword", mPasswordView.getText().toString());
				editor.commit();

				Intent intent = new Intent(SignupActivity.this,
						LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} else if (result == 1) {
				mPhoneNumView.setError("这个号码已经被注册啦！换一个吧！");
				mPhoneNumView.requestFocus();
			} else {
				Toast.makeText(SignupActivity.this, "注册失败！请检查网络连接！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
