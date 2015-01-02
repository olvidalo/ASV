/*
 * Copyright (C) 2015 Max Planck Institute for Psycholinguistics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mpi.metadatabrowser.wicket;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import nl.mpi.metadatabrowser.model.NodeAction;
import nl.mpi.metadatabrowser.model.NodeActionSingletonBean;
import org.apache.wicket.proxy.IProxyTargetLocator;
import org.apache.wicket.proxy.LazyInitProxyFactory;

/**
 * Utility class for converting lists of node actions to lists of node action
 * proxies that look up their targets via the Spring application context. This
 * prevents undesirable (de)serialisation of singleton beans.
 * @author Twan Goosen <twan.goosen@mpi.nl>
 */
public class NodeActionProxy {

    private final static NodeActionProxyConverter CONVERTER = new NodeActionProxyConverter();
    
    /**
     * Converts a list of node actions to a list of the same actions wrapped in
     * Spring bean proxies wherever applicable
     *
     * @param selectedNodeActions
     * @return
     */
    public static List<NodeAction> getProxyList(final List<NodeAction> selectedNodeActions) {
        if (selectedNodeActions == null) {
            return null;
        } else {
            return Lists.transform(selectedNodeActions, CONVERTER);
        }
    }

    public static class NodeActionProxyConverter extends Converter<NodeAction, NodeAction> implements Serializable {

        @Override
        protected NodeAction doForward(final NodeAction a) {
            if (a instanceof NodeActionSingletonBean) {
                // convert to a proxy; lookup by bean name
                final String beanName = ((NodeActionSingletonBean)a).getBeanName();
                return (NodeAction) LazyInitProxyFactory.createProxy(NodeAction.class, new IProxyTargetLocator() {

                    @Override
                    public Object locateProxyTarget() {
                        // get the bean from the application context
                        return MetadataBrowserApplication.get().getApplicationContext().getBean(beanName);
                    }
                });
            } else {
                // should not be converted to a proxy
                return a;
            }
        }

        @Override
        protected NodeAction doBackward(NodeAction b) {
            return b;
        }

    }
}
