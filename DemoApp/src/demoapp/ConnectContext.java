/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp;

import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.INConnector;

/**
 *
 * @author kaifa
 */
public class ConnectContext {

    public static String GetConnectKey(int id) {
        return "ClientID:" + id;
    }
    
    public int ClientID;
    public String MapKey;
    public INConnector Connector;
    public String SN;
    public boolean IsOpenWatch;
    public ConnectorDetail ConnectorDTL;
    public boolean Online;

    public ConnectContext(ConnectContext src) {
        SetNewContext(src);
    }

    public ConnectContext(int id, INConnector c, ConnectorDetail dtl) {
        ClientID = id;
        MapKey = GetConnectKey(id);
        Connector = c;
        IsOpenWatch = false;
        SN = null;
        ConnectorDTL = dtl;
        Online = true;
    }

    public void SetNewContext(ConnectContext ct) {
        ClientID = ct.ClientID;
        MapKey = ct.MapKey;
        Connector = ct.Connector;
        IsOpenWatch = ct.IsOpenWatch;
        SN = ct.SN;
        ConnectorDTL = ct.ConnectorDTL;
        Online = ct.Online;
    }

    public void SetOffline() {
        ClientID = 0;
        MapKey = null;
        Connector = null;
        IsOpenWatch = false;
        ConnectorDTL = null;
        Online = false;
    }

    public boolean IsOnline() {
        return Online;
    }
}
