package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StartNewSurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_survey);

        surveyType_spinner = (Spinner)findViewById(R.id.spinner_startnewsurvey_testtype);
        surveyType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                surveyType = surveyType_spinner.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        userType_spinner = (Spinner)findViewById(R.id.spinner_startnewsurvey_usertype);
        userType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                userType = userType_spinner.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        startBtn = (Button)findViewById(R.id.button_startnewsurvey_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(surveyType) {
                    case "직업적성검사":
                        //TBD
                        requestURL = "";

                        break;
                    case "직업가치관검사":
                        requestURL = "http://inspct.career.go.kr/openapi/test/questions?apikey=403f9bbdb00069287e869a9b302b406b&q=6";
                        break;
                    case "직업흥미검사":
                        if(userType.contentEquals("고등학생")) {
                            requestURL = "http://inspct.career.go.kr/openapi/test/questions?apikey=403f9bbdb00069287e869a9b302b406b&q=5";
                        } else if(userType.contentEquals("중학생")) {
                            requestURL = "http://inspct.career.go.kr/openapi/test/questions?apikey=403f9bbdb00069287e869a9b302b406b&q=4";
                        } else {
                            requestURL = "http://inspct.career.go.kr/openapi/test/questions?apikey=403f9bbdb00069287e869a9b302b406b&q=5";
                            Toast.makeText(getApplicationContext(),"해당 검사는 중,고등학생용입니다.",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "이공계가치관검사":
                        requestURL = "http://inspct.career.go.kr/openapi/test/questions?apikey=403f9bbdb00069287e869a9b302b406b&q=9";
                        break;
                    default:

                        break;
                }

                Log.d("###requestURL",requestURL);

                GetSurvey getSurvey = new GetSurvey();
                getSurvey.execute(requestURL);

//                Intent tempIntent = new Intent(StartNewSurveyActivity.this, PastResultActivity.class);
//                startActivity(tempIntent);
            }
        });

//        LoginTest loginTest = new LoginTest();
//        loginTest.execute("http://ec2-3-34-135-151.ap-northeast-2.compute.amazonaws.com/api/v1/users/signin/");
    }

    class GetSurvey extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(StartNewSurveyActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String result_string = result;

            Log.d("###PastResultActivity",result_string);
        }

        @Override
        protected String doInBackground(String...params) {

            String serverURL = (String)params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

//                GET 방식 사용할 경우, Body 붙여 보내면 error
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.flush();
//                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("###PastResultActivity", "GET response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d("###PastResultActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }

    class LoginTest extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(StartNewSurveyActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String result_string = result;

            Log.d("###PastResultActivity",result_string);
        }

        @Override
        protected String doInBackground(String...params) {
//            String input1 = (String)params[1];
//            String input2 = (String)params[2];

            String serverURL = (String)params[0];
//            String postParameters = "input1=" + input1; // + "&input2=" + input2;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", "test1");
//                jsonObject.put("email", "test@gamil.com");
//                jsonObject.put("name", "test1");
                jsonObject.put("input_password", "test1");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("###PastResultActivity", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d("###PastResultActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }
}