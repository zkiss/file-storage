package hu.bme.vihijv37.bus1fj.web.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import hu.bme.vihijv37.bus1fj.web.shared.dto.FileDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

@RemoteServiceRelativePath("fs")
public interface FsService extends RemoteService {

    /**
     * Visszaadja az összes file-t, melyet az adott user feltöltött
     * 
     * @param user
     * @return
     * @throws ServiceException
     */
    public Set<FileDto> getUserFiles(UserDto user) throws ServiceException;

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
     * @return aktuálisan regisztrált user
     * @throws ServiceException
     *             ha sikertelen a regisztráció
     */
    public UserDto register(String name, String email, String password) throws ServiceException;

    /**
     * A megadott file eltávolítása az adatbázisból
     * 
     * @param file
     * @throws ServiceException
     */
    public void removeFile(FileDto file) throws ServiceException;

    /**
     * User adatainak módosítása
     * 
     * @param user
     * @return módosított user
     * @throws ServiceException
     */
    public UserDto updateUser(UserDto user) throws ServiceException;
}
