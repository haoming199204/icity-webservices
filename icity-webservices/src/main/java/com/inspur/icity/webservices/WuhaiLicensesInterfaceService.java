package com.inspur.icity.webservices;

import com.iflytek.ursp.client.GsbService;
import com.iflytek.ursp.client.endpoint.ServiceEndPoint;
import com.iflytek.ursp.client.message.GsbDataType;
import com.iflytek.ursp.client.message.GsbParameter;
import com.iflytek.ursp.client.message.GsbResponse;
import com.iflytek.ursp.client.security.Algorithm;
import com.iflytek.ursp.client.session.ServiceSessionFactory;
import com.iflytek.ursp.client.session.Session;
import com.iflytek.ursp.client.transport.ITransport;
import com.inspur.icity.core.dareway.xml.XmlToMap;
import com.inspur.icity.core.utils.BeanUtil;
import com.inspur.icity.utils.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * 乌海证照接口
 *
 * @author haoming
 */
public class WuhaiLicensesInterfaceService implements IWuhaiLicensesInterfaceService {
    private static String appId = Config.getValue("wuhaiAppId");
    private static String authCode = Config.getValue("wuhaiAuthCode");
    private static String licensesUrl = Config.getValue("wuhaiLicensesUrl");
    private static String idCardServiceCode = Config.getValue("wuhaiLicencesIdCardIf");
    private static String drivingServiceCode = Config.getValue("wuhaiLicencesDrivingIf");
    private static String trafficServiceCode = Config.getValue("wuhaiLicencesTrafficIf");
    private static int interfaceVersion = Integer.parseInt(Config.getValue("wuhaiLicencesVersion"));
    private static String interfaceMethod = Config.getValue("wuhaiLicencesMethod");

    @Override
    public String getIdCardLicense(String cardNo, String name) {
        String condition = "<condition><item> <GMSFHM>" + cardNo + "</GMSFHM> <XM>" + name + "</XM> </item></condition>";
        String requiredItems = "<requiredItems><item> <CSRQ>出生日期</CSRQ> <GMSFHM>公民身份号码</GMSFHM> <MZ>MZ</MZ> <QFJG>签发机关</QFJG> <RYZP>人员照片</RYZP> <XB>性别</XB> <XM>姓名</XM> <YXQX>有效期限</YXQX> <ZZ>住址</ZZ></item></requiredItems>";
        String clientInfo = "<clientInfo><loginName>sadmin</loginName></clientInfo>";
        JSONObject result;
        JSONArray mapList = null;

        try {
            result = post(idCardServiceCode, condition, requiredItems, clientInfo);
            if (result != null) {
                mapList = result.getJSONArray("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return mapList;
        return mapList.toString();
    }

    @Override
    public String getDrivingLicense(String cardNo, String name) {
        String condition = "<condition><item> <GMSFHM>" + cardNo + "</GMSFHM> <XM>" + name + "</XM> </item></condition>";
        String requiredItems = "<requiredItems><item> <CCLZRQ>初次领证日期</CCLZRQ> <CSRQ>出生日期</CSRQ> <DABH>档案编号</DABH> <FZJG>发证机关</FZJG> <GJ>GJ</GJ> <GMSFHM>公民身份号码</GMSFHM> <LJJF>累计计分</LJJF> <XB>性别</XB> <XM>姓名</XM> <YXQS>有效期始</YXQS> <YXQZ>有效期至</YXQZ> <ZJCX>准驾车型</ZJCX> <ZZ>住址</ZZ> <JSZZT>驾驶证状态</JSZZT> </item></requiredItems>";
        String clientInfo = "<clientInfo><loginName>sadmin</loginName></clientInfo>";
        JSONObject result;
        JSONArray mapList = null;

        try {
            result = post(drivingServiceCode, condition, requiredItems, clientInfo);
            if (result != null) {
                mapList = result.getJSONArray("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return mapList;
        return mapList.toString();
    }

    @Override
    public String getTrafficLicense(String cardNo) {
        String condition = "<condition><item> <GMSFHM>" + cardNo + "</GMSFHM> </item></condition>";
        String requiredItems = "<requiredItems><item><BXZZRQ>保险终止日期</BXZZRQ><CCDJRQ>初次登记日期</CCDJRQ><CLLX>车辆类型</CLLX><CLPPZW>车辆中文品牌</CLPPZW><CLSBDM>车辆识别代码</CLSBDM><CLXH>车辆类型</CLXH><CLZT>车辆状态</CLZT><CWKC>车外廓长</CWKC><CWKG> 车外廓高</CWKG> <CWKK> 车外廓宽</CWKK> <FDJH> 发动机号</FDJH> <FXSZRQ> 发行驶证日期</FXSZRQ> <GMSFHM> 公民身份号码</GMSFHM> <HDZK> 核定载客</HDZK> <HPHM> 号牌号码</HPHM> <HPZL> 号牌种类</HPZL> <JDCSYR> 激动车所有人</JDCSYR> <JYYXQZ> 检验有效期至</JYYXQZ> <SYXZ> 使用性质</SYXZ> <ZBZL> 整备质量</ZBZL> <ZSXXDZ> 住所详细地址</ZSXXDZ> <ZZL> 总质量</ZZL> <CLYS> 车辆颜色</CLYS> </item></requiredItems>";
        String clientInfo = "<clientInfo><loginName>sadmin</loginName></clientInfo>";
        JSONObject result;
        JSONArray mapList = null;

        try {
            result = post(trafficServiceCode, condition, requiredItems, clientInfo);
            if (result != null) {
                mapList = result.getJSONArray("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return mapList;
        return mapList.toString();
    }

    private JSONArray resolveXML(String serviceCode, String xmlStr) {
        JSONArray list = new JSONArray();
        Map mapTmp;

        if (!BeanUtil.isNullString(xmlStr)) {
            try {
                mapTmp = XmlToMap.xml2map(xmlStr, true);
                if (serviceCode.equals(idCardServiceCode)) {
                    JSONObject responseJo = JSONObject.fromObject(mapTmp).getJSONObject("response");
                    JSONObject bodyJo = responseJo.getJSONObject("body");
                    Object resultListO = bodyJo.get("resultList");
                    if (resultListO instanceof JSONArray) {
                        JSONObject resultJo = JSONArray.fromObject(resultListO).getJSONObject(0).getJSONObject("result");
                        list.add(resultJo);
                    } else if (resultListO instanceof JSONObject) {
                        JSONObject resultJo = JSONObject.fromObject(resultListO).getJSONObject("result");
                        list.add(resultJo);
                    }
                } else if (serviceCode.equals(drivingServiceCode)) {
                    JSONObject responseJo = JSONObject.fromObject(mapTmp).getJSONObject("response");
                    JSONObject bodyJo = responseJo.getJSONObject("body");
                    Object resultListO = bodyJo.get("resultList");
                    if (resultListO instanceof JSONArray) {
                        JSONObject resultJo = JSONArray.fromObject(resultListO).getJSONObject(0).getJSONObject("result");
                        list.add(resultJo);
                    } else if (resultListO instanceof JSONObject) {
                        JSONObject resultJo = JSONObject.fromObject(resultListO).getJSONObject("result");
                        list.add(resultJo);
                    }
                } else if (serviceCode.equals(trafficServiceCode)) {
                    JSONObject responseJo = JSONObject.fromObject(mapTmp).getJSONObject("response");
                    JSONObject bodyJo = responseJo.getJSONObject("body");
                    Object resultListO = bodyJo.get("resultList");
                    if (resultListO instanceof JSONArray) {
                        JSONArray resultListJo = JSONArray.fromObject(resultListO);
                        for (int i = 0; i < resultListJo.size(); i++) {
                            JSONObject resultJo = resultListJo.getJSONObject(i).getJSONObject("result");
                            list.add(resultJo);
                        }
                    } else if (resultListO instanceof JSONObject) {
                        JSONObject resultJo = JSONObject.fromObject(resultListO).getJSONObject("result");
                        list.add(resultJo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        return list;
    }

    private JSONObject post(String serviceCode, String condition, String requiredItems, String clientInfo) {
        Session session = null;
        GsbResponse response = null;
        JSONObject map = new JSONObject();
        JSONArray resultList = new JSONArray();

        try {
            // 1.应用编码
            // 2.访问授权码
            ServiceSessionFactory serviceFactory = new ServiceSessionFactory(appId,
                    authCode);
            // 3.ursp地址
            ServiceEndPoint serviceEndPoint = new ServiceEndPoint(licensesUrl);
            serviceFactory.setServiceEndPoint(serviceEndPoint);

            // 4.服务编码，版本号，方法名称(rest服务此方法名没有太大含义,能够识别就好,webservice需要和调用的方法名一致)
            session = serviceFactory.openSession(new GsbService(serviceCode, interfaceVersion, interfaceMethod));

            // 默认不加密，可以指定加密算法，必须指定安全码
            session.setEncrypt(Algorithm.NONE);
            // 设置默认超时时间,ms
            session.setProperty(ITransport.TIME_OUT, 1000 * 180);

            // 被调用服务输入参数定义

            // 按照接口需求添加对应的参数,一个参数Add一次,如果调用的接口没有参数,就不需要调用
            session.addParameter(new GsbParameter("condition", GsbDataType.GsbString,
                    condition));
            session.addParameter(new GsbParameter("requiredItems", GsbDataType.GsbString,
                    requiredItems));
            session.addParameter(new GsbParameter("clientInfo", GsbDataType.GsbString,
                    clientInfo));


            // GSBResponse就是GSB应答
            response = session.request();
            // 请求成功
            if (response.getErrorCode() == 200) {
                resultList = resolveXML(serviceCode, response.getResponseMessage());

                map.put("code", response.getErrorCode());
                map.put("error", response.getErrorMsg());
                map.put("msg", response.getExceptionDetail());
                map.put("result", resultList);
            } else {
                map.put("code", response.getErrorCode());
                map.put("error", response.getErrorMsg());
                map.put("msg", response.getExceptionDetail());
                map.put("result", resultList);
            }
        } catch (Exception e) {
            if (response != null) {
                map.put("code", response.getErrorCode());
                map.put("error", response.getErrorMsg());
                map.put("msg", response.getExceptionDetail());
                map.put("result", resultList);
            }
        } finally {
            // 关闭会话
            session.close();
            return map;
        }

    }
}
