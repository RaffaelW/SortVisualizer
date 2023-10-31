package com.raffascript.sortvisualizer.visualization.data

enum class AlgorithmState {
    UNINITIALIZED, READY, RUNNING, PAUSED, FINISHED;
}

/*
 *                 Uninitialized
 *                      ||
 *                      \/
 *                     Ready <---------+
 *                      ||             | Restart
 *                      \/             |
 *         +--------> Running ---------|
 *  Resume |          ||   ||          |
 *         |          \/   \/          |
 *         +----- Paused  Finished ----|
 *                   |                 |
 *                   +-----------------+
 */