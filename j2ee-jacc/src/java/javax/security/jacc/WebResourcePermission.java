/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.security.jacc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 * @version $Rev$ $Date$
 */
public final class WebResourcePermission extends Permission implements Serializable {
    private transient int cachedHashCode = 0;
    private transient URLPatternSpec urlPatternSpec;
    private transient HTTPMethodSpec httpMethodSpec;

    public WebResourcePermission(HttpServletRequest request) {
        super(request.getServletPath());

        urlPatternSpec = new URLPatternSpec(request.getServletPath());
        httpMethodSpec = new HTTPMethodSpec(request);
    }

    public WebResourcePermission(String name, String actions) {
        super(name);

        // we do this because we're resusing the HTTPMethodSpec, which allows ':'
        if (actions.indexOf(':') != -1) {
            throw new IllegalArgumentException("Transports not allowed in WebResourcePermission httpMethodSpec");
        }

        urlPatternSpec = new URLPatternSpec(name);
        httpMethodSpec = new HTTPMethodSpec(actions);
    }

    public WebResourcePermission(String urlPattern, String[] HTTPMethods) {
        super(urlPattern);

        urlPatternSpec = new URLPatternSpec(urlPattern);
        httpMethodSpec = new HTTPMethodSpec(HTTPMethods);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WebResourcePermission)) return false;

        WebResourcePermission other = (WebResourcePermission) o;
        return urlPatternSpec.equals(other.urlPatternSpec) && httpMethodSpec.equals(other.httpMethodSpec);
    }

    public String getActions() {
        return httpMethodSpec.getActions();
    }

    public int hashCode() {
        if (cachedHashCode == 0) {
            cachedHashCode = urlPatternSpec.hashCode() ^ httpMethodSpec.hashCode();
        }
        return cachedHashCode;
    }

    public boolean implies(Permission permission) {
        if (permission == null || !(permission instanceof WebResourcePermission)) return false;

        WebResourcePermission other = (WebResourcePermission) permission;
        return urlPatternSpec.implies(other.urlPatternSpec) && httpMethodSpec.implies(other.httpMethodSpec);
    }

    public PermissionCollection newPermissionCollection() {
        return new WebResourcePermissionCollection();
    }

    private synchronized void readObject(ObjectInputStream in) throws IOException {
        urlPatternSpec = new URLPatternSpec(in.readUTF());
        httpMethodSpec = new HTTPMethodSpec(in.readUTF());
    }

    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(urlPatternSpec.getPatternSpec());
        out.writeUTF(httpMethodSpec.getActions());
    }

    private static final class WebResourcePermissionCollection extends PermissionCollection {
        private Hashtable permissions = new Hashtable();

        /**
         * Adds a permission object to the current collection of permission objects.
         *
         * @param permission the Permission object to add.
         *
         * @exception SecurityException -  if this PermissionCollection object
         *                                 has been marked readonly
         */
        public void add(Permission permission) {
            if (isReadOnly()) throw new IllegalArgumentException("Read only collection");

            if (!(permission instanceof WebResourcePermission)) throw new IllegalArgumentException("Wrong permission type");

            WebResourcePermission p  = (WebResourcePermission)permission;

            permissions.put(p, p);
        }

        /**
         * Checks to see if the specified permission is implied by
         * the collection of Permission objects held in this PermissionCollection.
         *
         * @param permission the Permission object to compare.
         *
         * @return true if "permission" is implied by the  permissions in
         * the collection, false if not.
         */
        public boolean implies(Permission permission) {
            if (!(permission instanceof WebResourcePermission)) return false;

            WebResourcePermission p  = (WebResourcePermission)permission;
            Enumeration e = permissions.elements();

            while (e.hasMoreElements()) {
                if (((WebResourcePermission)e.nextElement()).implies(p)) return true;
            }

            return false;
        }

        /**
         * Returns an enumeration of all the Permission objects in the collection.
         *
         * @return an enumeration of all the Permissions.
         */
        public Enumeration elements() {
            return permissions.elements();
        }
    }
}

