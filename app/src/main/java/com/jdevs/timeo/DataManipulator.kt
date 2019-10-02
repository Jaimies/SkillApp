package com.jdevs.timeo

open class DataManipulator {
    open fun writeFile() {

    }

    open fun readFile() {

    }
}

open class DataManipulatorFragment : FragmentWithActionBarNavigation()  {
    companion object Records: DataManipulator()
}