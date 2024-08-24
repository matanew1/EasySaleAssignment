package com.example.assignment.utils;

import androidx.fragment.app.Fragment;

/**
 * Interface for loading a fragment into a container.
 */
public interface ILoadFragment {

    /**
     * Loads a fragment into a container.
     * @param fragment The fragment to load.
     */
    void loadFragment(Fragment fragment);
}
