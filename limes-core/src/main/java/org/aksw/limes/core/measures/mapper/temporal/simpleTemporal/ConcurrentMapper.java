package org.aksw.limes.core.measures.mapper.temporal.simpleTemporal;

import org.aksw.limes.core.io.cache.Cache;
import org.aksw.limes.core.io.cache.Instance;
import org.aksw.limes.core.io.mapping.AMapping;
import org.aksw.limes.core.io.mapping.MappingFactory;
import org.aksw.limes.core.io.parser.Parser;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Implements the concurrent mapper class.
 *
 * @author Kleanthi Georgala <georgala@informatik.uni-leipzig.de>
 * @version 1.0
 */
public class ConcurrentMapper extends SimpleTemporalMapper {
    
    protected static final Logger logger = Logger.getLogger(ConcurrentMapper.class.getName());
    /**
     * Maps a set of source instances to their concurrent target instances. The
     * mapping contains 1-to-m relations. Each source instance takes as
     * concurrent events the set of target instances with the same begin date
     * property and the same machine id property of the source instance.
     *
     * @return a mapping, the resulting mapping
     */
    @Override
    public AMapping getMapping(Cache source, Cache target, String sourceVar, String targetVar, String expression,
            double threshold) {

        AMapping m = MappingFactory.createDefaultMapping();
        Parser p = new Parser(expression, threshold);

        TreeMap<String, Set<Instance>> sources = this.orderByBeginDate(source, expression);
        TreeMap<String, Set<Instance>> targets = this.orderByBeginDate(target, expression);
        String machineID = null;
        try {
            machineID = this.getSecondProperty(p.getLeftTerm());
        } catch (IllegalArgumentException e) {
            logger.error("Missing machine id property in " + p.getLeftTerm() + ".Exiting..");
            System.exit(1);
        }

        for (Map.Entry<String, Set<Instance>> sourceEntry : sources.entrySet()) {
            String epochSource = sourceEntry.getKey();

            Set<Instance> targetInstances = targets.get(epochSource);
            if (targetInstances != null) {
                Set<Instance> sourceInstances = sourceEntry.getValue();
                for (Instance i : sourceInstances) {
                    for (Instance j : targetInstances) {
                        if (i.getProperty(machineID).equals(j.getProperty(machineID)))
                            m.add(i.getUri(), j.getUri(), 1);
                    }
                }
            }
        }

        return m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Concurrent";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRuntimeApproximation(int sourceSize, int targetSize, double theta, Language language) {
        return 1000d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMappingSizeApproximation(int sourceSize, int targetSize, double theta, Language language) {
        return 1000d;
    }

}
