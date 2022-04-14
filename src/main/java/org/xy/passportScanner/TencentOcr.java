package org.xy.passportScanner;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.MLIDPassportOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.MLIDPassportOCRResponse;
import org.xy.passportScanner.config.GlobalData;
import java.util.Base64;

public class TencentOcr {
    private static final String region = "ap-guangzhou";
    /** 
     * @description
     * @author xy
     * @date 2022/04/13 15:10
     * @param image 
     * @param extension 
     * @return com.tencentcloudapi.ocr.v20181119.models.MLIDPassportOCRResponse 
     */
    public static MLIDPassportOCRResponse apiMLIDPassportOCR(byte[] image, String extension){
        try {
            Credential cred = new Credential(GlobalData.settings.getTencentSecretId(), GlobalData.settings.getTencentSecretKey());

            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, region, clientProfile);

            // 实例化一个请求对象,每个接口都会对应一个request对象
            MLIDPassportOCRRequest req = new MLIDPassportOCRRequest();

            //Image转Base64格式
            String base64 = Base64.getEncoder().encodeToString(image);
            req.setImageBase64("data:image/"+ extension +";base64," + base64);

            // 返回的resp是一个MLIDPassportOCRResponse的实例，与请求对象对应
            MLIDPassportOCRResponse resp = client.MLIDPassportOCR(req);
            // 输出json格式的字符串回包
            System.out.println(MLIDPassportOCRResponse.toJsonString(resp));
            return resp;

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
