package pl.shockah.godwit

import pl.shockah.godwit.swift.guard

class SnapshotList<E>(
		private val backingList: MutableList<E>
) : MutableList<E> by backingList {
	@PublishedApi
	internal val snapshottingLists = mutableListOf<SnapshottingList>()

	private fun snapshot() {
		if (!snapshottingLists.isEmpty()) {
			val snapshot = backingList.toList()
			snapshottingLists.forEach {
				it.snapshot = snapshot
				it.iterators.applyEach { switchToSnapshotIterator() }
			}
			snapshottingLists.clear()
		}
	}

	inline fun snapshot(code: (list: List<E>) -> Unit) {
		val snapshottingList = SnapshottingList()
		try {
			snapshottingLists += snapshottingList
			code(snapshottingList)
		} finally {
			snapshottingLists -= snapshottingList
		}
	}

	override fun add(element: E): Boolean {
		snapshot()
		return backingList.add(element)
	}

	override fun add(index: Int, element: E) {
		snapshot()
		return backingList.add(index, element)
	}

	override fun addAll(elements: Collection<E>): Boolean {
		snapshot()
		return backingList.addAll(elements)
	}

	override fun addAll(index: Int, elements: Collection<E>): Boolean {
		snapshot()
		return backingList.addAll(index, elements)
	}

	override fun remove(element: E): Boolean {
		snapshot()
		return backingList.remove(element)
	}

	override fun removeAt(index: Int): E {
		snapshot()
		return backingList.removeAt(index)
	}

	override fun removeAll(elements: Collection<E>): Boolean {
		snapshot()
		return backingList.removeAll(elements)
	}

	override fun retainAll(elements: Collection<E>): Boolean {
		snapshot()
		return backingList.retainAll(elements)
	}

	override fun set(index: Int, element: E): E {
		snapshot()
		return backingList.set(index, element)
	}

	override fun clear() {
		snapshot()
		return backingList.clear()
	}

	@PublishedApi
	internal inner class SnapshottingList : List<E> {
		internal var snapshot: List<E>? = null

		internal val iterators = mutableListOf<SnapshottingListIterator>()

		private inline val usedList: List<E>
			get() = snapshot ?: backingList

		override val size: Int
			get() = usedList.size

		override fun contains(element: E): Boolean {
			return usedList.contains(element)
		}

		override fun containsAll(elements: Collection<E>): Boolean {
			return usedList.containsAll(elements)
		}

		override fun isEmpty(): Boolean {
			return usedList.isEmpty()
		}

		override fun get(index: Int): E {
			return usedList[index]
		}

		override fun indexOf(element: E): Int {
			return usedList.indexOf(element)
		}

		override fun lastIndexOf(element: E): Int {
			return usedList.lastIndexOf(element)
		}

		override fun iterator(): Iterator<E> {
			return listIterator()
		}

		override fun listIterator(): ListIterator<E> {
			return listIterator(0)
		}

		override fun listIterator(index: Int): ListIterator<E> {
			val iterator = SnapshottingListIterator(index)
			iterators += iterator
			return iterator
		}

		override fun subList(fromIndex: Int, toIndex: Int): List<E> {
			throw UnsupportedOperationException()
		}

		internal inner class SnapshottingListIterator(
				private val initialPosition: Int = 0
		) : ListIterator<E> {
			private var iterator: ListIterator<E> = backingList.listIterator(initialPosition)

			private var position = 0

			internal fun switchToSnapshotIterator() {
				val snapshot = snapshot.guard { return }
				iterator = snapshot.listIterator(initialPosition)
				for (i in 0 until position) {
					iterator.next()
				}
				for (i in 0 until -position) {
					iterator.previous()
				}
			}

			override fun hasNext(): Boolean {
				return iterator.hasNext()
			}

			override fun hasPrevious(): Boolean {
				return iterator.hasPrevious()
			}

			override fun nextIndex(): Int {
				return iterator.nextIndex()
			}

			override fun previousIndex(): Int {
				return iterator.previousIndex()
			}

			override fun next(): E {
				val element = iterator.next()
				position++
				return element
			}

			override fun previous(): E {
				val element = iterator.previous()
				position--
				return element
			}
		}
	}
}