/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.vpc.service.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.vpc.model.Vpc;
import org.openo.sdno.vpc.nbi.VpcNbiServiceImpl;
import org.openo.sdno.vpc.nbi.inf.IVpcNbiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * VPC service class for ROA.
 * <br/>
 * <p>
 * </p>
 *
 * @author
 * @version     SDNO 0.5  2016-7-07
 */
@Service
@Path("/svc/vpc/v1")
public class VpcServiceROAResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpcServiceROAResource.class);

    @Resource
    IVpcNbiService service = new VpcNbiServiceImpl();


    public IVpcNbiService getService() {
        return this.service;
    }


    public void setService(IVpcNbiService service) {
        this.service = service;
    }

    /**
     * Rest interface to perform create vpc operation. <br/>
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param vpcBody Vpc Object
     * @return The object of ResultRsp
     * @throws ServiceException When create vpc failed
     * @since SDNO 0.5
     */
    @POST
    @Path("/vpcs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            String vpcBody) throws ServiceException {
        LOGGER.info("START.");
        long infterEnterTime = System.currentTimeMillis();

        //TODO(mrkanag) find tenant

        Vpc vpc = JsonUtil.fromJson(vpcBody, Vpc.class);
        //TODO(mrkanag) Validate model.

        vpc = this.service.create(vpc);

        if (resp != null) {
            resp.setStatus(HttpStatus.SC_CREATED);
        }

        LOGGER.info("END. cost time = " + (System.currentTimeMillis() - infterEnterTime));

        return JsonUtil.toJson(vpc);
    }

    /**
     * Rest interface to perform query vpc operation. <br/>
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param vpcId The uuid of vpc
     * @return The object of ResultRsp
     * @throws ServiceException When query vpc failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/vpcs/{vpc_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String query(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("vpc_id") String vpcId) throws ServiceException {

        Vpc vpc = this.service.get(vpcId);

        if (resp != null) {
            resp.setStatus(HttpStatus.SC_OK);
        }

        return JsonUtil.toJson(vpc);
    }

    //TODO(mrkanag) impl list & update on need basis

    /**
     * Rest interface to perform delete vpc operation. <br/>
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     * @param vpcId The uuid of Vpc
     * @return The object of ResultRsp
     * @throws ServiceException When delete vpc failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/vpcs/{vpc_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("vpc_id") String vpcId) throws ServiceException {
        LOGGER.debug("START.");
        long infterEnterTime = System.currentTimeMillis();

        this.service.delete(vpcId);

        if (resp != null) {
            resp.setStatus(HttpStatus.SC_NO_CONTENT);
        }

        LOGGER.info("END. cost time = " + (System.currentTimeMillis() - infterEnterTime));
    }
}
