package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

@RemoteServiceRelativePath("fs")
public interface FsService extends RemoteService {

    /**
     * Bejelentkezés.
     * 
     * @param email
     *            a felhasználó e-mail címe
     * @param password
     *            a felhasználó jelszava
     * @return A bejelentkezett felhasználó
     * @throws ServiceException
     *             ha sikertelen a bejelentkezés
     */
    public UserDto login(String email, String password) throws ServiceException;

    /**
     * Regisztráció.
     * 
     * @param name
     *            a felhasználó neve
     * @param email
     *            a felhasználó email címe
     * @param password
     *            a felhasználó jelszava
     * 
     * @return
     * @throws ServiceException
     *             ha sikertelen a regisztráció
     */
    public UserDto register(String name, String email, String password) throws ServiceException;

}
