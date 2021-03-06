package cloudos.main;

import cloudos.model.support.RestoreRequest;
import cloudos.resources.setup.SetupSettings;
import org.cobbzilla.wizard.task.TaskId;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.util.json.JsonUtil;
import org.cobbzilla.wizard.client.ApiClientBase;

import java.io.File;

import static cloudos.resources.ApiConstants.SETUP_ENDPOINT;
import static org.cobbzilla.util.json.JsonUtil.fromJson;
import static org.cobbzilla.util.json.JsonUtil.toJson;

@Slf4j
public class RestoreCloudsteadMain extends CloudOsMainBase<RestoreCloudsteadOptions> {

    public static final File FIRST_TIME_SETUP_FILE = new File("/home/cloudos/.first_time_setup");

    public static void main (String[] args) { main(RestoreCloudsteadMain.class, args); }

    @Override
    protected void run() throws Exception {
        final ApiClientBase api = getApiClient();
        final RestoreCloudsteadOptions options = getOptions();
        final String restoreUri = SETUP_ENDPOINT + "/restore";

        // a bit ugly; find a better way or require the end user to specify it
        final String setupKey = JsonUtil.fromJson(FIRST_TIME_SETUP_FILE, SetupSettings.class).getSecret();

        final RestoreRequest restoreRequest = new RestoreRequest()
                .setRestoreKey(options.getKey())
                .setNotifyEmail(options.getNotifyEmail())
                .setSetupKey(setupKey)
                .setInitialPassword(options.getPassword());

        final TaskId taskId = fromJson(api.post(restoreUri, toJson(restoreRequest)).json, TaskId.class);
        out("Restore initiated. TaskId:\n"+toJson(taskId)+"\n");
    }
}
