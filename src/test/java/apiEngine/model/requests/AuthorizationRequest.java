package apiEngine.model.requests;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.google.gson.annotations.SerializedName;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AuthorizationRequest {
    @NonNull
    @SerializedName(value = "userName")
    private String username;

    @NonNull
    @SerializedName("password")
    private String password;

}