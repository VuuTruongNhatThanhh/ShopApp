package com.example.basicman.retrofit;

import com.example.basicman.model.NotiResponse;
import com.example.basicman.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAA0vwZGP8:APA91bHoisi71Ya2SqfagwUjEWT9_7jLnb--XRXdcvn_Q3OlhowtG8qE92NVxB4D78G9YNTgMI5Ng8w9sRxb__J9LtgBAnkR-m2U4Ii95tMjhWRCpYRzfbcSOZcLn-rS1B5ZqMi6IItz"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data);
}
