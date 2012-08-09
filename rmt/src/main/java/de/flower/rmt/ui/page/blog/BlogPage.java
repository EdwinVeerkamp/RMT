package de.flower.rmt.ui.page.blog;

import de.flower.rmt.service.IBlogManager;
import de.flower.rmt.service.security.ISecurityService;
import de.flower.rmt.ui.page.base.AbstractCommonBasePage;
import de.flower.rmt.ui.page.base.NavigationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author flowerrrr
 */
public class BlogPage extends AbstractCommonBasePage {

    public BlogPage() {
        setHeading("blog.heading", "blog.heading.sub");
        addMainPanel(new ArticleListPanel());
        addSecondaryPanel(new BlogSecondaryPanel());
        add(new BlogMarkReadBehavior());
    }

    @Override
    public String getActiveTopBarItem() {
        return NavigationPanel.BLOG;
    }

    public static class BlogMarkReadBehavior extends Behavior {

        @SpringBean
        private IBlogManager blogManager;

        @SpringBean
        private ISecurityService securityService;

        @Override
        public void onConfigure(final Component component) {
            super.onConfigure(component);
            blogManager.markAllRead(securityService.getUser());
        }
    }
}
