/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package javax.mail.search;

import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * @version $Rev$ $Date$
 */
public final class SentDateTerm extends DateTerm {
	
	private static final long serialVersionUID = 5647755030530907263L;
	
    public SentDateTerm(int comparison, Date date) {
        super(comparison, date);
    }

    public boolean match(Message message) {
        try {
            Date date = message.getSentDate(); 
            if (date == null) {
                return false; 
            }
            
            return match(message.getSentDate());
        } catch (MessagingException e) {
            return false;
        }
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof SentDateTerm == false) return false;
        return super.equals(other);
    }
}
