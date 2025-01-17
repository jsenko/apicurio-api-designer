/*
 * Copyright 2021 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.designer.test.run.mt;

import io.apicurio.common.apps.multitenancy.TenantManagerService;
import io.apicurio.common.apps.multitenancy.exceptions.TenantNotAuthorizedException;
import io.apicurio.common.apps.multitenancy.exceptions.TenantNotFoundException;
import io.apicurio.tenantmanager.api.datamodel.ApicurioTenant;
import io.apicurio.tenantmanager.api.datamodel.NewApicurioTenantRequest;
import io.apicurio.tenantmanager.api.datamodel.TenantStatusValue;
import io.quarkus.test.Mock;
import io.vertx.core.impl.ConcurrentHashSet;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carles Arnal
 */
@Mock
public class MockTenantMetadataService extends TenantManagerService {

    private static final Map<String, ApicurioTenant> cache = new ConcurrentHashMap<String, ApicurioTenant>();

    private static final Set<String> unauthorizedList = new ConcurrentHashSet<>();

    /**
     * @see TenantManagerService#getTenant(String)
     */
    @Override
    public ApicurioTenant getTenant(String tenantId) throws TenantNotFoundException {
        if (unauthorizedList.contains(tenantId)) {
            throw new TenantNotAuthorizedException("Tenant not authorized");
        }

        var tenant = cache.get(tenantId);
        if (tenant == null) {
            throw new TenantNotFoundException("not found " + tenantId);
        }
        return tenant;
    }


    @Override
    public ApicurioTenant createTenant(NewApicurioTenantRequest tenant) {
        System.out.println("Creating tenant " + tenant.getTenantId());
        ApicurioTenant apicurioTenant = new ApicurioTenant();
        apicurioTenant.setCreatedBy(tenant.getCreatedBy());
        apicurioTenant.setCreatedOn(Date.from(Instant.now()));
        apicurioTenant.setTenantId(tenant.getTenantId());
        apicurioTenant.setStatus(TenantStatusValue.READY);
        apicurioTenant.setOrganizationId(tenant.getOrganizationId());
        cache.put(tenant.getTenantId(), apicurioTenant);

        return apicurioTenant;
    }

    public void addToUnauthorizedList(String tenantId) {
        unauthorizedList.add(tenantId);
    }

}
