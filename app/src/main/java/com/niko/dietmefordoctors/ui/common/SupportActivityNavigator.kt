package com.niko.dietmefordoctors.ui.common

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

class SupportActivityNavigator(val activity: FragmentActivity) : Navigator {

    override fun applyCommands(commands: Array<out Command>) {
        for (command in commands) {
            applyCommand(command)
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    private fun applyCommand(command: Command) {
        when (command) {
            is Forward -> forward(command)
            is Back -> back()
            is Replace -> replace(command)
        }
    }

    /**
     * Override this method to create option for start activity
     *
     * @param command        current navigation command. Will be only [Forward] or [Replace]
     * @param activityIntent activity intent
     * @return transition options
     */
    private fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
        return null
    }

    private fun back() {
        exit()
    }

    private fun forward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        // Start activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
        }
    }

    private fun replace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        // Replace activity
        if (activityIntent != null) run {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
            activity.finish()
        }
    }


    private fun checkAndStartActivity(screen: SupportAppScreen, activityIntent: Intent, options: Bundle?) {
        // Check if we can start activity
        if (activityIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }

    /**
     * Called when there is no activity to open `screenKey`.
     *
     * @param screenKey screen key
     * @param activityIntent intent passed to start Activity for the `screenKey`
     */
    fun unexistingActivity(screen: SupportAppScreen, activityIntent: Intent) {
        // Do nothing by default
    }

    private fun exit() {
        // Finish by default
        activity.finish()
    }
}