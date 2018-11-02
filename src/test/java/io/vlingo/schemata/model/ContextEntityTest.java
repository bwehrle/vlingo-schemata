// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.schemata.model;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.actors.testkit.TestWorld;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ContextEntityTest {
    private TestWorld world;
    private TestActor<Context> contextTestActor;

    @Before
    public void setUp() throws Exception {
        world = TestWorld.start("context-test");
        contextTestActor = world.actorFor(Definition.has(ContextEntity.class, Definition.parameters(Id.OrganizationId.unique(), Id.UnitId.unique(),
                Id.ContextId.unique(), "namespace", "desc")), Context.class);
    }

    @After
    public void tearDown() {
        world.terminate();
    }

    @Test
    public void testThatContextRenamed() throws Exception {
        contextTestActor.actor().changeNamespaceTo("newNamespace");
        //Assertion for change namespace
        final Events.ContextRenamed contextRenamed = (Events.ContextRenamed) ((ArrayList) contextTestActor.viewTestState().valueOf("applied")).get(1);
        Assert.assertEquals("newNamespace", contextRenamed.namespace);
    }

    @Test
    public void testThatContextIsDescribed() throws Exception {
        contextTestActor.actor().describeAs("newDesc");
        //Assertion for description
        final Events.ContextDescribed contextDescribed = (Events.ContextDescribed) ((ArrayList) contextTestActor.viewTestState().valueOf("applied")).get(1);
        Assert.assertEquals("newDesc", contextDescribed.description);
    }

    @Test
    public void testThatContextDefinedIsEquals() throws Exception {
        final Events.ContextDefined contextDefined = (Events.ContextDefined) ((ArrayList) contextTestActor.viewTestState().valueOf("applied")).get(0);
        final Events.ContextDefined newContextDefined = new Events.ContextDefined(Id.OrganizationId.existing(contextDefined.organizationId), Id.UnitId.existing(contextDefined.unitId),
                Id.ContextId.existing(contextDefined.contextId), contextDefined.namespace, contextDefined.description);
        Assert.assertEquals(newContextDefined, contextDefined);
    }

    @Test
    public void testThatContextRenamedIsEquals() throws Exception {
        contextTestActor.actor().changeNamespaceTo("newNamespace");
        final Events.ContextRenamed contextRenamed = (Events.ContextRenamed) ((ArrayList) contextTestActor.viewTestState().valueOf("applied")).get(1);
        final Events.ContextRenamed newContextRenamed = new Events.ContextRenamed(Id.OrganizationId.existing(contextRenamed.organizationId), Id.UnitId.existing(contextRenamed.unitId),
                Id.ContextId.existing(contextRenamed.contextId), contextRenamed.namespace);
        Assert.assertEquals(contextRenamed, newContextRenamed);
    }

    @Test
    public void testThatContextDescribedIsEquals() throws Exception {
        contextTestActor.actor().describeAs("newDesc");
        final Events.ContextDescribed contextDescribed = (Events.ContextDescribed) ((ArrayList) contextTestActor.viewTestState().valueOf("applied")).get(1);
        final Events.ContextDescribed newContextDescribed = new Events.ContextDescribed(Id.OrganizationId.existing(contextDescribed.organizationId), Id.UnitId.existing(contextDescribed.unitId),
                Id.ContextId.existing(contextDescribed.contextId), contextDescribed.description);
        Assert.assertEquals(contextDescribed, newContextDescribed);
    }


}