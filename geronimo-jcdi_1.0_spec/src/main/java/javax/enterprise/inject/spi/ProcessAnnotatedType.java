/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

/**
 * Event is fired before reading any annotations on the class.
 * 
 * @version $Rev$ $Date$
 *
 *  <X> class type
 */
public interface ProcessAnnotatedType<X>
{
    /**
     * Gets annotated type.
     * 
     * @return annotated type
     */
    public AnnotatedType<X> getAnnotatedType();
    
    /**
     * Replaces annotated type.
     * 
     * @param type annotated type
     */
    public void setAnnotatedType(AnnotatedType<X> type);
    
    /**
     * Veto registering process.
     */
    public void veto();    

}
