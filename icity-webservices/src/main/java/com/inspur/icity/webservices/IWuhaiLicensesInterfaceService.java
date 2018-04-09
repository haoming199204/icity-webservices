package com.inspur.icity.webservices;

/**
 * @Name ${NAME}
 * @Description TODO
 * @Author haoming
 * @Date 2018/2/26
 * @Version 1.0
 */
public interface IWuhaiLicensesInterfaceService extends java.rmi.Remote {
    String getIdCardLicense(String cardNo, String name) throws java.rmi.RemoteException;

    String getDrivingLicense(String cardNo, String name) throws java.rmi.RemoteException;

    String getTrafficLicense(String cardNo) throws java.rmi.RemoteException;
}
