package org.aksw.limes.core.execution.engine;

import static org.junit.Assert.*;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class ExecutionEngineFactoryTest{

    
    public ExecutionEngineFactoryTest() {
    }

    
    @Before
    public void setUp() {

    }
    @After 
    public void tearDown() {

    }
    @Test
    public void testEqualDefault() {
	ExecutionEngine engine = ExecutionEngineFactory.getEngine("default", null, null, null, null);
	assertTrue(engine instanceof SimpleExecutionEngine);
    }
    @Test
    public void testEqualParallel() {
	ExecutionEngine engine = ExecutionEngineFactory.getEngine("parallel", null, null, null, null);
	assertTrue(engine instanceof ParallelExecutionEngine);
    }
    
    @Test
    public void testNotEqualParallel() {
	ExecutionEngine engine = ExecutionEngineFactory.getEngine("parallel", null, null, null, null);
	assertFalse(engine instanceof SimpleExecutionEngine);
    }
    
    

    
   
    
}
