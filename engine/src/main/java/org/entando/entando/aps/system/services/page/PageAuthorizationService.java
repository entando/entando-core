package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.system.services.authorization.AuthorizationManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.auth.AuthorizationService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paddeo
 */
public class PageAuthorizationService extends AuthorizationService<PageDto> {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private AuthorizationManager authorizationManager;

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    public AuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public boolean isAuth(UserDetails user, PageDto pageDto) {
        return this.isAuth(user, pageDto.getCode());
    }

    @Override
    public boolean isAuth(UserDetails user, String pageCode) {
        IPage page = this.getPageManager().getDraftPage(pageCode);
        return this.isAuth(user, page);
    }

    public boolean isAuth(UserDetails user, IPage page) {
        return this.getAuthorizationManager().isAuth(user, page);
    }

    @Override
    public List<PageDto> filterList(UserDetails user, List<PageDto> toBeFiltered) {
        List<PageDto> res = new ArrayList<>();
        Optional.ofNullable(toBeFiltered).ifPresent(elements -> res.addAll(elements.stream()
                .filter(elem -> this.isAuth(user, elem)).collect(Collectors.toList())));
        return res;
    }

}
